package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Discount;
import fpt.swp.pcols.service.BrandService;
import fpt.swp.pcols.service.CategoryService;
import fpt.swp.pcols.service.DiscountService;
import fpt.swp.pcols.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/discount")
public class DiscountController {
    private final DiscountService discountService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    // List all discounts
    @GetMapping
    public String listDiscounts(Model model) {
        List<Discount> discounts = discountService.getAllDiscounts();
        model.addAttribute("discounts", discounts);
        return "admin/discount/list";
    }

    // Show create form
    @GetMapping("/new")
    public String createDiscountForm(Model model) {
        model.addAttribute("discount", new Discount());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/discount/create";
    }

    // Handle create form submission
    @PostMapping
    public String createDiscount(@Valid @ModelAttribute Discount discount,
                                 BindingResult result,
                                 @RequestParam("applyTo") String applyTo,
                                 @RequestParam(value = "productId", required = false) Long productId,
                                 @RequestParam(value = "categoryId", required = false) Long categoryId,
                                 @RequestParam(value = "brandId", required = false) Long brandId,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("discount", discount);
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            return "admin/discount/create";
        }
        discountService.createDiscount(discount, applyTo, productId, categoryId, brandId);
        return "redirect:/admin/discount";
    }

    // Show edit form
    @GetMapping("/{id}/edit")
    public String editDiscountForm(@PathVariable Long id, Model model) {
        Discount discount = discountService.getDiscountById(id);
        model.addAttribute("discount", discount);
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        String applyTo = discount.getProduct() != null ? "product" :
                discount.getCategory() != null ? "category" :
                        discount.getBrand() != null ? "brand" : "";
        model.addAttribute("applyTo", applyTo);
        return "admin/discount/edit";
    }

    // Handle edit form submission
    @PostMapping("/{id}")
    public String updateDiscount(@PathVariable Long id, @ModelAttribute Discount discount,
                                 @RequestParam("applyTo") String applyTo,
                                 @RequestParam(value = "productId", required = false) Long productId,
                                 @RequestParam(value = "categoryId", required = false) Long categoryId,
                                 @RequestParam(value = "brandId", required = false) Long brandId) {
        discountService.updateDiscount(id, discount, applyTo, productId, categoryId, brandId);
        return "redirect:/admin/discount";
    }

    // Deactivate a discount
    @PostMapping("/{id}/deactivate")
    public String deactivateDiscount(@PathVariable Long id) {
        discountService.deactivateDiscount(id);
        return "redirect:/admin/discount";
    }
}
