package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
