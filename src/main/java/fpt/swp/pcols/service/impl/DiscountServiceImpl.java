package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.dto.DiscountDTO;
import fpt.swp.pcols.entity.Brand;
import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Discount;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.repository.DiscountRepository;
import fpt.swp.pcols.service.BrandService;
import fpt.swp.pcols.service.CategoryService;
import fpt.swp.pcols.service.DiscountService;
import fpt.swp.pcols.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;


    @Override
    public List<Discount> getAllDiscounts() {
        return this.discountRepository.findAll();
    }

    @Override
    public Discount getDiscountById(Long id) {
        if (discountRepository.findDiscountById(id) != null) {
            return discountRepository.findDiscountById(id);
        } else {
            throw new IllegalArgumentException("Discount not found");
        }
    }

    @Override
    public void createDiscount(Discount discount, String applyTo, Long productId, Long categoryId, Long brandId) {
        setAppliedTo(discount, applyTo, productId, categoryId, brandId);
        discountRepository.save(discount);
    }

    @Override
    public void updateDiscount(Long id, Discount discount, String applyTo, Long productId, Long categoryId, Long brandId) {
        Discount existing = getDiscountById(id);
        existing.setDiscountValue(discount.getDiscountValue());
        existing.setDiscountType(discount.getDiscountType());
        existing.setStartDate(discount.getStartDate());
        existing.setEndDate(discount.getEndDate());
        existing.setIsActive(discount.getIsActive());
        setAppliedTo(existing, applyTo, productId, categoryId, brandId);
        discountRepository.save(existing);
    }

    @Override
    public void deactivateDiscount(Long id) {
        Discount discount = getDiscountById(id);
        discount.setIsActive(false);
        discountRepository.save(discount);
    }

    @Override
    public void setAppliedTo(Discount discount, String applyTo, Long productId, Long categoryId, Long brandId) {
        // Reset all associations
        discount.setProduct(null);
        discount.setCategory(null);
        discount.setBrand(null);

        // Set the selected association
        switch (applyTo) {
            case "product":
                if (productId != null) {
                    Product product = productService.getProductById(productId);
                    discount.setProduct(product);
                }
                break;
            case "category":
                if (categoryId != null) {
                    Category category = categoryService.getCategoryById(categoryId);
                    discount.setCategory(category);
                }
                break;
            case "brand":
                if (brandId != null) {
                    Brand brand = brandService.getBrandById(brandId);
                    discount.setBrand(brand);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid applyTo value");
        }

        // Validate that exactly one target is set
        if (discount.getProduct() == null && discount.getCategory() == null && discount.getBrand() == null) {
            throw new IllegalArgumentException("A discount must be applied to a product, category, or brand");
        }
    }

    @Override
    public Map<Long, DiscountDTO> getProductDiscounts(List<Product> products) {
        Map<Long, DiscountDTO> discountMap = new HashMap<>();

        products.forEach(product -> {
            // Lấy tất cả discount active cho product
            List<Discount> activeDiscounts = discountRepository.findActiveDiscountsForProduct(
                    product.getId(),
                    product.getCategory().getId(),
                    product.getBrand().getId(),
                    LocalDateTime.now()
            );

            if (!activeDiscounts.isEmpty()) {
                Discount bestDiscount = selectBestDiscount(activeDiscounts);
                discountMap.put(
                        product.getId(),
                        calculateDiscountDTO(product.getPrice(), bestDiscount)
                );
            }
        });

        return discountMap;
    }

    @Override
    public DiscountDTO calculateDiscountDTO(double originalPrice, Discount discount) {
        BigDecimal discountValue = discount.getDiscountValue();
        double discountedPrice = originalPrice;

        if (discount.getDiscountType() == Discount.DiscountType.PERCENTAGE) {
            discountedPrice = originalPrice * (1 - discountValue.doubleValue() / 100);
        } else {
            discountedPrice = originalPrice - discountValue.doubleValue();
        }

        return new DiscountDTO(discountValue, discount.getDiscountType(), discountedPrice);
    }

    @Override
    public Discount selectBestDiscount(List<Discount> discounts) {
        return discounts.stream()
                .max(Comparator.comparing(this::calculateDiscountValueImpact))
                .orElseThrow();
    }

    @Override
    public double calculateDiscountValueImpact(Discount discount) {
        return discount.getDiscountValue().doubleValue();
    }

    @Override
    public Discount getDiscountByProduct(Long productId) {
        Optional<Discount> discountOpt = discountRepository.findByProductId(productId);
        return discountOpt.orElse(null);
    }

}