package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.ReviewFormDTO;
import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.Review;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.service.CategoryService;
import fpt.swp.pcols.service.ProductService;
import fpt.swp.pcols.service.ReviewService;
import fpt.swp.pcols.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        List<Product> gearProducts = productService.getProductsByCategoryWithImages("Gear")
                .stream().limit(3)
                .collect(Collectors.toList());
        List<Product> monitorProducts = productService.getProductsByCategoryWithImages("Monitor")
                .stream().limit(3)
                .collect(Collectors.toList());
        List<Product> pcProducts = productService.getProductsByCategoryWithImages("PC")
                .stream().limit(3)
                .collect(Collectors.toList());
        model.addAttribute("gearProducts", gearProducts);
        model.addAttribute("monitorProducts", monitorProducts);
        model.addAttribute("pcProducts", pcProducts);

        // New code: Load all categories dynamically and prepare a map of category name to up to 3 products
        List<Category> allCategories = categoryService.getAllCategories();
        System.out.println("All Categories: " + allCategories);

        Map<String, List<Product>> categoryProductsMap = new HashMap<>();
        for (Category category : allCategories) {
            List<Product> products = productService.getProductsByCategory(category.getName())
                    .stream().limit(4)
                    .collect(Collectors.toList());
            categoryProductsMap.put(category.getName(), products);
        }
        model.addAttribute("categoryProductsMap", categoryProductsMap);
        System.out.println("categoryProductsMap = " + categoryProductsMap);

        return "home";
    }

    @GetMapping("/chatbot")
    public String chatBot() {
        return "/fragments/chatbot";
    }

    @GetMapping("/product-detail")
    public String getProductDetail(@RequestParam("id") Long productId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "3") int size,
                                   Model model) {
        Product product = productService.getProductById(productId);
        Category category = product.getCategory();
        model.addAttribute("product", product);
        model.addAttribute("category", category);

        model.addAttribute("relatedProducts", productService.getRelatedProducts(product, 4));

        Page<Review> reviewPage = reviewService.getReviewsByProduct(product, page, size);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());

        model.addAttribute("reviewForm", new ReviewFormDTO("", null));

        return "product-detail";
    }

    @PostMapping("/product-detail/review")
    public String submitReview(@RequestParam Long productId,
                               @ModelAttribute("reviewForm") ReviewFormDTO reviewForm,
                               @AuthenticationPrincipal User user,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(productId);

        ValidationResult validationResult = reviewService.validateReviewForm(reviewForm);
        if (validationResult.isHasErrors()) {
            model.addAllAttributes(validationResult.getErrors());

            Category category = product.getCategory();
            model.addAttribute("product", product);
            model.addAttribute("category", category);
            model.addAttribute("relatedProducts", productService.getRelatedProducts(product, 4));

            Page<Review> reviewPage = reviewService.getReviewsByProduct(product, 0, 3);
            model.addAttribute("reviews", reviewPage.getContent());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", reviewPage.getTotalPages());

            return "product-detail";
        }

        if (user == null) {
            return "redirect:/auth/login";
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(reviewForm.rating());
        review.setComment(reviewForm.comment());
        reviewService.saveReview(review);

        redirectAttributes.addFlashAttribute("successMessage", "Review submitted successfully");
        return "redirect:/product-detail?id=" + productId;
    }


}


