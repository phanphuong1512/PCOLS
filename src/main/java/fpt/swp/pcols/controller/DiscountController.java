package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Discount;
import fpt.swp.pcols.service.BrandService;
import fpt.swp.pcols.service.CategoryService;
import fpt.swp.pcols.service.DiscountService;
import fpt.swp.pcols.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        List<Discount> discounts = discountService.findAll();
        model.addAttribute("discounts", discounts);
        return "admin/discount/list";
    }

    // Show create form
    @GetMapping("/new")
    public String createDiscountForm(Model model) {
        model.addAttribute("discount", new Discount());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
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
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("brands", brandService.findAll());
            return "admin/discount/create";
        }
        discountService.createDiscount(discount, applyTo, productId, categoryId, brandId);
        return "redirect:/admin/discount";
    }

    // Show edit form
    @GetMapping("/{id}/detail")
    public String showDiscountDetail(@PathVariable Long id, Model model) {
        Discount discount = discountService.findById(id).orElseThrow(() -> new RuntimeException("Discount not found"));
        model.addAttribute("discount", discount);

        String applyTo = "";
        String appliedName = "";

        if (discount.getProduct() != null) {
            applyTo = "Product";
            appliedName = discount.getProduct().getName();
        } else if (discount.getCategory() != null) {
            applyTo = "Category";
            appliedName = discount.getCategory().getName();
        } else if (discount.getBrand() != null) {
            applyTo = "Brand";
            appliedName = discount.getBrand().getName();
        }

        model.addAttribute("applyTo", applyTo);
        model.addAttribute("appliedName", appliedName);

        return "admin/discount/detail";
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
    // Toggle discount status (active/inactive)
    @PostMapping("/{id}/toggle-status")
    public String toggleDiscountStatus(@PathVariable Long id) {
        discountService.toggleDiscountStatus(id);
        return "redirect:/admin/discount";
    }
}
