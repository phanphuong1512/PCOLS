package com.swp392.PCOLS.repository;

import com.swp392.PCOLS.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getCategoryById(long id);
    Category save(Category category);

    List<Category> findAll();
}