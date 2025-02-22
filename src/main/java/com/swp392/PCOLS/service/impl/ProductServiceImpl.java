package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.repository.ProductRepository;
import com.swp392.PCOLS.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductList() {
        return List.of();
    }

    @Override
    public String addProduct(Product product) {
        return "";
    }

    @Override
    public Product handleSaveProduct(Product product) {
        return null;
    }
}
