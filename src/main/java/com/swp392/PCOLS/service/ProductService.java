package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();

    void handleSaveProduct(Product product);

    Product getProductById(long id);
}
