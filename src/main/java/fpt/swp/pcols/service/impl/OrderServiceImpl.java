package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.dto.BillDTO;
import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.exception.OutOfStockException;
import fpt.swp.pcols.repository.OrderDetailRepository;
import fpt.swp.pcols.repository.OrderRepository;
import fpt.swp.pcols.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceImpl productService;

    @Override
    public Order save(Order cart) {
        return orderRepository.save(cart);
    }

    @Override
    public Optional<Order> getCurrentCartForUser(User user) {
        return orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING);
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


}