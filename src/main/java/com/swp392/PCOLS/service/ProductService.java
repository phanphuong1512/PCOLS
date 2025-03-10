package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.Category;
import com.swp392.PCOLS.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();

    String addProduct(Product product);

    Product handleSaveProduct(Product product);

    Product getProductById(long id);

    List<Product> getProductsByCategory(Long categoryId);

    List<Product> getAllProductsSortedBy(String field, String order);

    Page<Product> getAllProductsPaginated(int page, int size);
    Page<Product> getAllProductsByPricePaginated(int page, int size, String sort);
    Page<Product> getProductsByCategoryPaginated(Long categoryId, int page, int size);
}
