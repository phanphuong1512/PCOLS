package com.swp392.PCOLS.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "category") // ✅ Prevents infinite recursion
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "brand")
    private String brand;

    @Column(name = "stock")
    private int stock;

    @Column(name = "image")
    private String image;

    // Correct @ManyToOne mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id") // Foreign key column
    private Category category;
}