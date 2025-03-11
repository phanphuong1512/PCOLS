package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.entity.Category;
import com.swp392.PCOLS.repository.CategoryRepository;
import com.swp392.PCOLS.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


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
    public Category getCategoryById(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

}
