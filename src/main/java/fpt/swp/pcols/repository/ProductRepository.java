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

    @Query("SELECT p FROM Product p WHERE " +
            "(:brand IS NULL OR p.brand = :brand) " +
            "AND (:category IS NULL OR p.category.name = :category) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> findFilteredProducts(@Param("brand") String brand,
                                       @Param("category") String category,
                                       @Param("minPrice") Double minPrice,
                                       @Param("maxPrice") Double maxPrice,
                                       String sort);
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.brand IS NOT NULL")
    List<String> findAllBrands();

    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryName IS NULL OR p.category.name = :categoryName) AND " +
            "(:brandName IS NULL OR p.brand = :brandName) AND " +
            "(:name IS NULL OR p.name LIKE %:name%)")
    List<Product> findProducts(@Param("categoryName") String categoryName,
                               @Param("brandName") String brandName,
                               @Param("name") String name);
}
