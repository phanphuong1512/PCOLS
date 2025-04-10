package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> findById(Long selectedCategoryId);

    List<Category> findAll();


}
