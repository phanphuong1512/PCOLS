package com.swp392.PCOLS.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "Category_Name")
    private String categoryName;

    @ManyToMany(mappedBy = "category")
    private List<Product> products;
}
