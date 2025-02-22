package com.swp392.PCOLS.entity;

import jakarta.persistence.*;

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "category_id")
    private Category category;
}
