package com.swp392.PCOLS.repository;

import com.swp392.PCOLS.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE " +
            "(:brand IS NULL OR p.brand = :brand) " +
            "AND (:category IS NULL OR p.category.name = :category) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> findFilteredProducts(@Param("brand") String brand,
                                       @Param("category") String category,
                                       @Param("minPrice") Double minPrice,
                                       @Param("maxPrice") Double maxPrice,
                                       Sort sort);
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.brand IS NOT NULL")
    List<String> findAllBrands();
}
