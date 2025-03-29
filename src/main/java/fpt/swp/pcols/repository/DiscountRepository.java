package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Discount findDiscountById(Long id);
}
