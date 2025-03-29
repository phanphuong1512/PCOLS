package fpt.swp.pcols.dto;

import fpt.swp.pcols.entity.Discount;

import java.math.BigDecimal;

public record DiscountDTO(
        BigDecimal discountValue,
        Discount.DiscountType discountType,
        double discountedPrice
) {
}