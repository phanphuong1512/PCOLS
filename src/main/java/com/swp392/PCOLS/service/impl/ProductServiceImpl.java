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
    public List<Product> getFilteredProducts(String brand, String category, Double minPrice, Double maxPrice, String sort) {
        Sort sortOption = Sort.unsorted();
        if ("asc".equalsIgnoreCase(sort)) {
            sortOption = Sort.by("price").ascending();
        } else if ("desc".equalsIgnoreCase(sort)) {
            sortOption = Sort.by("price").descending();
        }

        return productRepository.findFilteredProducts(brand, category, minPrice, maxPrice, sortOption);
    }

    @Override
    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }


}
