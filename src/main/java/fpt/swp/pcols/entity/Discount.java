package fpt.swp.pcols.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Data
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_value", nullable = false)
    @DecimalMin(value = "0.0", message = "Discount value must be at least 0")
    @DecimalMax(value = "100.0", message = "Discount value cannot exceed 100 for percentage discounts")
    private BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "start_date")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @Column(name = "is_active", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean isActive = true;

    public enum DiscountType {
        PERCENTAGE,
        FIXED
    }

    // Custom validation for date range
    @AssertTrue(message = "End date must be after the start date")
    public boolean isValidDateRange() {
        return endDate == null || startDate == null || endDate.isAfter(startDate);
    }
}