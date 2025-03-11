package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.repository.ProductRepository;
import com.swp392.PCOLS.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    @Override
    public void handleSaveProduct(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public Product getProductById(long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }
}
