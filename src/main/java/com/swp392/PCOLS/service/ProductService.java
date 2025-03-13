package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();

    void handleSaveProduct(Product product);

    Product getProductById(long id);

    Page<Product> getAllProductsSortedByPrice(int page, int size, Sort.Direction direction);

    List<Product> getAllProducts();

    Page<Product> getAllProductsPaginated(int page, int size);
}
