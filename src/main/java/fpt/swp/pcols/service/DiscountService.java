package fpt.swp.pcols.service;

import fpt.swp.pcols.dto.DiscountDTO;
import fpt.swp.pcols.entity.Discount;
import fpt.swp.pcols.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DiscountService {

    List<Discount> findAll();

    Optional<Discount> findById(Long id);

    void createDiscount(Discount discount, String applyTo, Long productId, Long categoryId, Long brandId);

    void updateDiscount(Long id, Discount discount, String applyTo, Long productId, Long categoryId, Long brandId);

    void deactivateDiscount(Long id);

    void setAppliedTo(Discount discount, String applyTo, Long productId, Long categoryId, Long brandId);

    Map<Long, DiscountDTO> getProductDiscounts(List<Product> products);

    DiscountDTO calculateDiscountDTO(double originalPrice, Discount discount);

    Discount selectBestDiscount(List<Discount> discounts);

    double calculateDiscountValueImpact(Discount discount);

    Discount getDiscountByProduct(Long productId);
}
