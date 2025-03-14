package com.swp392.PCOLS.repository;

import com.swp392.PCOLS.entity.Category;
import com.swp392.PCOLS.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);
    Product findById(long id);
    Product getProductById(long id);

    List<Product> getProductsByCategory(Long categoryId);
}
