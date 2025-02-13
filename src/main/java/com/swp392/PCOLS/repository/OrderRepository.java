package com.swp392.PCOLS.repository;

import com.swp392.PCOLS.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
