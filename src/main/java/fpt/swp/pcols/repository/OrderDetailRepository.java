package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    OrderDetail findByOrderAndProductId(Order order, Long productId);
}