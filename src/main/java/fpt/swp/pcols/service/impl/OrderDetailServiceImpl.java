package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.repository.OrderDetailRepository;
import fpt.swp.pcols.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;


    @Override
    public OrderDetail findByOrderAndProduct_Id(Order cart, Long productId) {
        return orderDetailRepository.findByOrderAndProduct_Id(cart, productId);
    }

    @Override
    public Optional<OrderDetail> findById(Long detailId) {
        return orderDetailRepository.findById(detailId);
    }

    @Override
    public void delete(OrderDetail detail) {
        orderDetailRepository.delete(detail);
    }

    @Override
    public void save(OrderDetail detail) {
        orderDetailRepository.save(detail);
    }
}
