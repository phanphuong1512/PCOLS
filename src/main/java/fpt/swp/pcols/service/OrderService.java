package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import fpt.swp.pcols.entity.User;

import java.util.Optional;

public interface OrderService {

    Order save(Order cart);

    Optional<Order> getCurrentCartForUser(User user);

    OrderDetail findOrderDetailByOrderAndProduct(Order cart, Long productId);

    Optional<OrderDetail> findDetailById(Long detailId);

    void deleteDetail(OrderDetail detail);

    void saveDetail(OrderDetail detail);

}