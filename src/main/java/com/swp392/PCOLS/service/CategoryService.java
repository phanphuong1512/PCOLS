package com.swp392.PCOLS.service;

import com.swp392.PCOLS.entity.Category;
import com.swp392.PCOLS.entity.Product;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategory();
    Category handleSaveCategory(Category category);

    Category getCategoryById(Long selectedCategoryId);


    List<Category> getAllCategories();
}
