package fpt.swp.pcols.service;

import fpt.swp.pcols.dto.ReviewFormDTO;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.Review;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.validation.ValidationResult;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Page<Review> getReviewsByProduct(Product product, int page, int size);

    Optional<Review> findReviewByUserAndProduct(User user, Product product);

    void save(Review review);

    ValidationResult validateReviewForm(ReviewFormDTO reviewForm);

    double calculateAverageRating(Long productId);

    List<Review> getRecentReviews(int limit);
}
