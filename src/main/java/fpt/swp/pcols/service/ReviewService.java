package fpt.swp.pcols.service;

import fpt.swp.pcols.dto.ReviewFormDTO;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.Review;
import fpt.swp.pcols.validation.ValidationResult;
import org.springframework.data.domain.Page;

public interface ReviewService {

    Page<Review> getReviewsByProduct(Product product, int page, int size);

    void saveReview(Review review);

    ValidationResult validateReviewForm(ReviewFormDTO reviewForm);
}
