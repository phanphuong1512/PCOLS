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
    public Optional<Category> findById(Long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

}
