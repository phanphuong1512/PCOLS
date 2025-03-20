package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Name(String categoryName);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.category = :category")
    List<Product> findByCategoryWithImages(@Param("category") Category category);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<Product> findByIdWithImages(@Param("id") Long id);

}
