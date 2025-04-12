package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Product;

import java.util.List;

public interface AIService {
    List<Product> findDiscountedProducts();

    List<Product> findTopRatedProducts();
}
