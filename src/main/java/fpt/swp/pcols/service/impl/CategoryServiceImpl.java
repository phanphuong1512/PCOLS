package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.repository.CategoryRepository;
import fpt.swp.pcols.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    Optional<Category> findByName(String name){
        return this.categoryRepository.findByName(name);
    }
}
