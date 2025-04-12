package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.DiscountDTO;
import fpt.swp.pcols.dto.ReviewFormDTO;
import fpt.swp.pcols.entity.*;
import fpt.swp.pcols.service.DiscountService;
import fpt.swp.pcols.service.OrderService;
import fpt.swp.pcols.service.ProductService;
import fpt.swp.pcols.service.ReviewService;
import fpt.swp.pcols.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProductDetailController {
    private final ProductService productService;
    private final ReviewService reviewService;
    private final DiscountService discountService;
    private final OrderService orderService;

    @GetMapping("/product-detail")
    public String getProductDetail(@RequestParam("id") Long productId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "3") int size,
                                   Model model) {
        Product product = productService.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        Category category = product.getCategory();
        Brand brand = product.getBrand();
        List<Product> relatedProducts = productService.getRelatedProducts(product, 4);
        Page<Review> reviewPage = reviewService.getReviewsByProduct(product, page, size);

        List<Product> allDisplayProducts = new ArrayList<>();
        allDisplayProducts.add(product);
        allDisplayProducts.addAll(relatedProducts);

        // 4. Lấy thông tin discount cho tất cả sản phẩm
        Map<Long, DiscountDTO> discountMap = discountService.getProductDiscounts(allDisplayProducts);


        Map<Long, Double> averageRatings = new HashMap<>();
        averageRatings.put(product.getId(), reviewService.calculateAverageRating(productId));
        for (Product relatedProduct : relatedProducts) {
            averageRatings.put(relatedProduct.getId(), reviewService.calculateAverageRating(relatedProduct.getId()));
        }

        model.addAttribute("averageRating", averageRatings);
        model.addAttribute("product", product);
        model.addAttribute("category", category);
        model.addAttribute("brand", brand);
        model.addAttribute("relatedProducts", relatedProducts);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("reviewForm", new ReviewFormDTO("", null));
        model.addAttribute("discountMap", discountMap); // Thêm discountMap vào model

        return "product-detail";
    }

    @GetMapping("/product-detail/review")
    public String getReviews(@RequestParam("productId") Long productId,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "3") int size,
                             Model model) {
        Product product = productService.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        Page<Review> reviewPage = reviewService.getReviewsByProduct(product, page, size);
        model.addAttribute("reviews", reviewPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reviewPage.getTotalPages());
        model.addAttribute("product", product);
        return "fragments/reviews"; // Trả về fragment
    }

    @PostMapping("/product-detail/review")
    public ResponseEntity<String> submitReview(@RequestParam Long productId,
                                               @ModelAttribute("reviewForm") ReviewFormDTO reviewForm,
                                               @AuthenticationPrincipal User user) {
        // Kiểm tra đăng nhập
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Please login to submit a review");
        }

        // Lấy sản phẩm theo productId
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // Validate form review
        ValidationResult validationResult = reviewService.validateReviewForm(reviewForm);
        if (validationResult.isHasErrors()) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> errors = validationResult.getErrors();
            if (errors.containsKey("ratingError")) {
                sb.append(errors.get("ratingError")).append(" ");
            }
            if (errors.containsKey("commentError")) {
                sb.append(errors.get("commentError")).append(" ");
            }
            return ResponseEntity.badRequest().body(sb.toString().trim());
        }

        // Kiểm tra xem user đã mua sản phẩm này chưa
        if (!orderService.hasUserPurchasedProduct(user, product)) {
            return ResponseEntity.badRequest()
                    .body("You can only review products you have purchased.");
        }

        // Kiểm tra xem user đã có review cho sản phẩm này chưa
        Optional<Review> existingReview = reviewService.findReviewByUserAndProduct(user, product);
        if (existingReview.isPresent()) {
            // Nếu đã có, cập nhật review cũ với thông tin mới
            Review reviewToUpdate = existingReview.get();
            reviewToUpdate.setRating(reviewForm.rating());
            reviewToUpdate.setComment(reviewForm.comment());
            // Cập nhật lại thời gian tạo, nếu cần cập nhật thời gian review
            reviewToUpdate.setCreatedAt(LocalDateTime.now());
            reviewService.save(reviewToUpdate);
            return ResponseEntity.ok("Review updated successfully");
        } else {
            // Nếu chưa có, tạo review mới
            Review review = new Review();
            review.setProduct(product);
            review.setUser(user);
            review.setRating(reviewForm.rating());
            review.setComment(reviewForm.comment());
            reviewService.save(review);
            return ResponseEntity.ok("Review submitted successfully");
        }
    }
}
