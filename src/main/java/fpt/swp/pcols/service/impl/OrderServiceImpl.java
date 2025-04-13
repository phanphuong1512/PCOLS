package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.dto.BillDTO;
import fpt.swp.pcols.dto.DashboardStatsDTO;
import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.exception.OutOfStockException;
import fpt.swp.pcols.repository.OrderRepository;
import fpt.swp.pcols.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductServiceImpl productService;

    @Override
    public boolean hasUserPurchasedProduct(User user, Product product) {
        // Giả sử các trạng thái đơn hàng hợp lệ là PAID, PACKED và SHIPPED
        List<Order.OrderStatus> validStatuses = Arrays.asList(Order.OrderStatus.PAID, Order.OrderStatus.PACKED, Order.OrderStatus.SHIPPED);
        // Lấy danh sách đơn hàng của user có trạng thái hợp lệ
        List<Order> orders = orderRepository.findByUserAndStatusIn(user, validStatuses);

        // Nếu không có đơn hàng nào, trả về false
        if (orders == null || orders.isEmpty()) {
            return false;
        }

        // Kiểm tra từng đơn hàng xem có chứa sản phẩm được review không.
        return orders.stream().anyMatch(order ->
                order.getOrderDetails().stream()
                        .anyMatch(orderDetail -> orderDetail.getProduct().getId().equals(product.getId()))
        );
    }


    @Override
    public Order save(Order cart) {
        return orderRepository.save(cart);
    }

    @Override
    public Optional<Order> getCurrentCartForUser(User user) {
        logger.info("Tìm kiếm order (giỏ hàng) cho user: {}", user.getUsername());
        Optional<Order> orderOptional = orderRepository.findByUserAndStatus(user, Order.OrderStatus.CART);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            logger.debug("Tìm thấy order với ID: {} có {} orderDetails", order.getId(),
                    order.getOrderDetails() != null ? order.getOrderDetails().size() : 0);
            return orderOptional;
        } else {
            logger.warn("Không tìm thấy giỏ hàng (order với status CART) cho user: {}. Tạo giỏ hàng mới.", user.getUsername());
            Order newCart = new Order();
            newCart.setUser(user);
            newCart.setStatus(Order.OrderStatus.CART);
            // Optionally, các field khác có thể được khởi tạo nếu cần (ví dụ: orderDate)
            newCart.setOrderDate(LocalDateTime.now());
            Order savedCart = orderRepository.save(newCart);
            return Optional.of(savedCart);
        }
    }

    @Override
    public Optional<Order> getLatestPlacedOrderForUser(User user) {
        // Nếu nghiệp vụ cho rằng đơn hàng xác nhận có status PENDING, bạn lấy đơn hàng mới nhất với status PENDING
        return orderRepository.findTopByUserAndStatusOrderByOrderDateDesc(user, Order.OrderStatus.PENDING);
    }


    @Override
    public List<Order> getFilteredOrders(String sort, Order.OrderStatus status, String email) {
        Sort sortOrder = Sort.by("orderDate");
        if ("asc".equalsIgnoreCase(sort)) {
            sortOrder = sortOrder.ascending();
        } else {
            sortOrder = sortOrder.descending();
        }
        return orderRepository.findOrders(status, email, sortOrder);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return this.orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

    @Transactional
    @Override
    public void confirmOrder(Order order, BillDTO billDTO) {
        Map<Long, String> errorMap = new HashMap<>();
        // Kiểm tra và giảm stock cho các sản phẩm trong đơn hàng
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Product product = orderDetail.getProduct();
            int availableStock = product.getStock();
            int orderedQuantity = orderDetail.getQuantity();

            if (orderedQuantity > availableStock) {
                // Ghi lỗi cho chi tiết sản phẩm này (sử dụng detail.getId())
                errorMap.put(orderDetail.getId(),
                        String.format("Not enough stock for product: %s (Ordered: %d, Available: %d)",
                                product.getName(), orderedQuantity, availableStock));
            } else {
                // Nếu đủ hàng, cập nhật stock và lưu sản phẩm
                product.setStock(availableStock - orderedQuantity);
                productService.save(product);
            }
        }

        // Nếu có lỗi, ném lỗi ra ngoài
        if (!errorMap.isEmpty()) {
            throw new OutOfStockException(errorMap);
        }

        // Cập nhật thông tin order
        order.setFirstName(billDTO.firstName());
        order.setLastName(billDTO.lastName());
        order.setEmail(billDTO.email());
        order.setAddress(billDTO.address());
        order.setCity(billDTO.city());
        order.setCountry(billDTO.country());
        order.setZipCode(billDTO.zipCode());
        order.setPhone(billDTO.phone());
        order.setShippingMethod(billDTO.shipping());
        order.setPaymentMethod(billDTO.payment());

        // Cập nhật trạng thái đơn hàng và lưu
        order.setStatus(Order.OrderStatus.PENDING);
        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(status);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order with ID " + orderId + " not found.");
        }
    }

    @Override
    public DashboardStatsDTO getDashboardStats(int year) {
        List<Order> paidOrders = orderRepository.findByYear(year);

        int totalOrders = paidOrders.size();

        // Sales = number of orders with PAID status
        int sales = totalOrders;

        // Earnings = sum of all orderDetail prices * quantity for PAID orders
        BigDecimal earnings = paidOrders.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .map(od -> od.getPrice().multiply(BigDecimal.valueOf(od.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardStatsDTO(totalOrders, sales, earnings);
    }

    @Override
    public List<Integer> getYearRangeForOrders() {
        Integer startYear = orderRepository.findEarliestOrderYear();
        int currentYear = LocalDateTime.now().getYear();

        if (startYear == null) {
            return List.of(currentYear); // fallback if no orders
        }

        List<Integer> years = new ArrayList<>();
        for (int y = startYear; y <= currentYear; y++) {
            years.add(y);
        }
        return years;
    }

    @Override
    public List<Order> getOrdersByYear(int year) {
        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(year, 12, 31, 23, 59, 59);

        return orderRepository.findByOrderDateBetweenAndStatusNot(
                startOfYear,
                endOfYear,
                Order.OrderStatus.CART
        );
    }
}