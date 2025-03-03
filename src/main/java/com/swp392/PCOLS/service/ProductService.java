package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProduct();
    String addProduct(Product product);
    Product handleSaveProduct(Product product);

    Product getProductById(long id);
}
