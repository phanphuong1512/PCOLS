package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.repository.OrderDetailRepository;
import fpt.swp.pcols.repository.OrderRepository;
import fpt.swp.pcols.service.OrderService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public Order save(Order cart) {
        return orderRepository.save(cart);
    }

    @Override
    public Optional<Order> getCurrentCartForUser(User user) {
        return orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING);
    }

    @Override
    public OrderDetail findOrderDetailByOrderAndProduct(Order cart, Long productId) {
        return orderDetailRepository.findByOrderAndProductId(cart, productId);
    }

    @Override
    public Optional<OrderDetail> findDetailById(Long detailId) {
        return orderDetailRepository.findById(detailId);
    }

    @Override
    public void deleteDetail(OrderDetail detail) {
        orderDetailRepository.delete(detail);
    }

    @Override
    public void saveDetail(OrderDetail detail) {
        orderDetailRepository.save(detail);
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
    public Order getOrderById(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

}