package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByProduct_Id(Long productId);

    @Query("SELECT d FROM Discount d WHERE " +
            "(d.product.id = :productId OR d.category.id = :categoryId OR d.brand.id = :brandId) " +
            "AND d.isActive = true " +
            "AND d.startDate <= :now " +
            "AND d.endDate >= :now")
    List<Discount> findActiveDiscountsForProduct(
            @Param("productId") Long productId,
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("now") LocalDateTime now
    );

    @Modifying
    @Query("UPDATE Discount d SET d.isActive = false WHERE d.id = :id")
    void deactivateById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Discount d SET d.isActive = CASE WHEN d.isActive = true THEN false ELSE true END WHERE d.id = :id")
    void toggleActiveStatus(@Param("id") Long id);
}
