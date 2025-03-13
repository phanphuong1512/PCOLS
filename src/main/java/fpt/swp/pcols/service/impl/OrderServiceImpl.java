package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.repository.OrderRepository;
import fpt.swp.pcols.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

}
