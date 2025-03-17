package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndStatus(User user, Order.OrderStatus status);
}