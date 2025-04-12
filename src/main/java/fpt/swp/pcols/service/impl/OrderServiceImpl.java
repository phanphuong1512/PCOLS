package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.dto.BillDTO;
import fpt.swp.pcols.dto.DashboardStatsDTO;
import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.exception.OutOfStockException;
import fpt.swp.pcols.repository.OrderDetailRepository;
import fpt.swp.pcols.repository.OrderRepository;
import fpt.swp.pcols.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductServiceImpl productService;

    @Override
    public Order save(Order cart) {
        return orderRepository.save(cart);
    }

    @Override
    public Optional<Order> getCurrentCartForUser(User user) {
        logger.info("Tìm kiếm order cho user: {}", user.getUsername());
        Optional<Order> orderOptional = orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            logger.debug("Tìm thấy order với ID: {}", order.getId());

            // Kiểm tra orderDetails
            if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
                logger.warn("Order ID: {} không có orderDetails", order.getId());
            } else {
                logger.debug("Order ID: {} có {} orderDetails", order.getId(), order.getOrderDetails().size());
                // Kiểm tra subtotal
                BigDecimal subtotal = order.getSubtotal();
                logger.debug("Subtotal của order ID: {} là {}", order.getId(), subtotal);
                if (subtotal == null) {
                    logger.warn("Subtotal của order ID: {} là null", order.getId());
                }
            }
        } else {
            logger.warn("Không tìm thấy order PENDING cho user: {}", user.getUsername());
        }

        return orderOptional;
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
        List<Order> paidOrders = orderRepository.findByYear( year);

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


}