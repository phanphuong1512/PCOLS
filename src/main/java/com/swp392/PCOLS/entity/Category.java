package com.swp392.PCOLS.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "Category_Name")
    private String categoryName;

    @ManyToMany(mappedBy = "category")
    private List<Product> products;
}
