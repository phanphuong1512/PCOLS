package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.repository.OrderRepository;
import com.swp392.PCOLS.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

}
