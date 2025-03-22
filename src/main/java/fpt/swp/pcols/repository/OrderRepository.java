package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserAndStatus(User user, Order.OrderStatus status);


    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderDetails od " +
            "JOIN FETCH od.product p " +
            "JOIN FETCH p.images " +
            "WHERE o.user = :user AND o.status = :status")
    Optional<Order> findByUserAndStatusWithDetails(@Param("user") User user, @Param("status") Order.OrderStatus status);
}
