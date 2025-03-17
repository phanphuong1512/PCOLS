package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.repository.OrderDetailRepository;
import fpt.swp.pcols.repository.OrderRepository;
import fpt.swp.pcols.service.OrderService;
import org.springframework.stereotype.Service;

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
}