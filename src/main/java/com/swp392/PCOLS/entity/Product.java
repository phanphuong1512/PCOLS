package com.swp392.PCOLS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column
    private String productName;

    @Column
    private double price;

    @Column
    private String description;

    @Column
    private String image;

    @ManyToMany
    @JoinTable(name = "category", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Category> category;
}
