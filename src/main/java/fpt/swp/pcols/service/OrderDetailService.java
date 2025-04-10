package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;

import java.util.Optional;

public interface OrderDetailService {
    OrderDetail findByOrderAndProduct_Id(Order cart, Long productId);

    Optional<OrderDetail> findById(Long detailId);

    void delete(OrderDetail detail);

    void save(OrderDetail detail);
}
