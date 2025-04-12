package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Order;
import fpt.swp.pcols.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserAndStatus(User user, Order.OrderStatus status);

    Optional<Order> findTopByUserAndStatusOrderByOrderDateDesc(User user, Order.OrderStatus status);


    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderDetails od " +
            "JOIN FETCH od.product p " +
            "JOIN FETCH p.images " +
            "WHERE o.user = :user AND o.status = :status")
    Optional<Order> findByUserAndStatusWithDetails(@Param("user") User user, @Param("status") Order.OrderStatus status);

    @Query("SELECT o FROM Order o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:email IS NULL OR o.email LIKE %:email%)")
    List<Order> findOrders(@Param("status") Order.OrderStatus status,
                           @Param("email") String email,
                           Sort sort);

    @Query("SELECT o FROM Order o WHERE YEAR(o.orderDate) = :year")
    List<Order> findByYear(@Param("year") int year);

    @Query("SELECT MIN(YEAR(o.orderDate)) FROM Order o")
    Integer findEarliestOrderYear();

    List<Order> findByUserAndStatusIn(User user, List<Order.OrderStatus> validStatuses);

    List<Order> findByOrderDateBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, Order.OrderStatus excludeStatus);
}
