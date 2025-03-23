package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory();

    Category getCategoryById(Long selectedCategoryId);

    List<Category> getAllCategories();


}
