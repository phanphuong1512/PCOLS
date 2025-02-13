package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.Order;
import com.swp392.PCOLS.repository.OrderRepository;
import com.swp392.PCOLS.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void save(Order order) {
        this.orderRepository.save(order);
    }
}
