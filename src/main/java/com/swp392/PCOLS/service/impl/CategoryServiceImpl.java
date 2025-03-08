package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.Category;
import com.swp392.PCOLS.repository.CategoryRepository;
import com.swp392.PCOLS.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategory() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category handleSaveCategory(Category category) {
        this.categoryRepository.save(category);
        return category;
    }

    @Override
    public Category getCategoryById(Long selectedCategoryId) {
        return this.categoryRepository.getCategoryById(selectedCategoryId);
    }

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

}
