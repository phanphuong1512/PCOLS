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
    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    @Override
    public String addProduct(Product product) {
        return "";
    }

    @Override
    public Product handleSaveProduct(Product product) {
        this.productRepository.save(product);
        return product;
    }

    @Override
    public Product getProductById(long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return this.productRepository.getProductsByCategory(categoryId);
    }
}
