package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Discount;

import java.util.List;

public interface DiscountService {
    List<Discount> getAllDiscounts();
    Discount getDiscountById(Long id);
    void createDiscount(Discount discount, String applyTo, Long productId, Long categoryId, Long brandId);
    void updateDiscount(Long id, Discount discount, String applyTo, Long productId, Long categoryId, Long brandId);
    void deactivateDiscount(Long id);
    void setAppliedTo(Discount discount, String applyTo, Long productId, Long categoryId, Long brandId);

}
