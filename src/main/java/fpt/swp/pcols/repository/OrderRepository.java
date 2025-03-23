package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndStatus(User user, Order.OrderStatus status);
}
