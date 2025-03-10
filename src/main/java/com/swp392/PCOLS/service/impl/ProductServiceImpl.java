package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.Category;
import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.repository.ProductRepository;
import com.swp392.PCOLS.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        return this.productRepository.findByCategory_Id(categoryId);
    }

    @Override
    public List<Product> getAllProductsSortedBy(String field, String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return productRepository.findAll(Sort.by(Sort.Direction.DESC, field));
        }
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    @Override
    public Page<Product> getAllProductsPaginated(int page, int size) {
        return this.productRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Product> getAllProductsByPricePaginated(int page, int size, String sort) {
        Sort sortOrder = Sort.by("price");
        if ("price_desc".equals(sort)) {
            sortOrder = Sort.by("price").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return this.productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getProductsByCategoryPaginated(Long categoryId, int page, int size) {
        return this.productRepository.findByCategory_Id(categoryId, PageRequest.of(page, size));
    }


}
