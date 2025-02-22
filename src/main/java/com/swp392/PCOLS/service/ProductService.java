package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.repository.ProductRepository;
import com.swp392.PCOLS.web.mvc.ProductController;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    List<Product> getProductList();
    String addProduct(Product product);
    Product handleSaveProduct(Product product);

}
