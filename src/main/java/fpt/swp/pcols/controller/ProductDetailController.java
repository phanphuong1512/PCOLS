package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.ReviewFormDTO;
import fpt.swp.pcols.entity.*;
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

@Controller
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping("/product-detail")
    public String getProductDetail(@RequestParam("id") Long productId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "3") int size,
                                   Model model) {
        Product product = productService.getProductById(productId);
        Category category = product.getCategory();
        Brand brand = product.getBrand();
        model.addAttribute("product", product);
        model.addAttribute("category", category);
        model.addAttribute("brand", brand);

        List<Product> relatedProducts = productService.getRelatedProducts(product, 4);
        model.addAttribute("relatedProducts", relatedProducts);

        Page<Review> reviewPage = reviewService.getReviewsByProduct(product, page, size);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("reviewForm", new ReviewFormDTO("", null));

        Map<Long, Double> averageRatings = new HashMap<>();
        averageRatings.put(product.getId(), reviewService.calculateAverageRating(productId));
        for (Product relatedProduct : relatedProducts) {
            averageRatings.put(relatedProduct.getId(), reviewService.calculateAverageRating(relatedProduct.getId()));
        }
        model.addAttribute("averageRating", averageRatings);

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
