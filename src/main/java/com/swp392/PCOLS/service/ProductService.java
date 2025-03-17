package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();

    void handleSaveProduct(Product product);

    Product getProductById(long id);

    List<Product> getAllProducts();

    List<Product> getFilteredProducts(String brand, String category, Double minPrice, Double maxPrice, String sort);

    List<String> getAllBrands();
}
