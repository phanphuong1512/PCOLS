package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.repository.ProductRepository;
import com.swp392.PCOLS.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllProductsSortedByPrice(int page, int pageSize, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(direction, "price"));
        return productRepository.findAll(pageable);
    }
}
