package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.dto.ReviewFormDTO;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.Review;
import fpt.swp.pcols.repository.ReviewRepository;
import fpt.swp.pcols.service.ReviewService;
import fpt.swp.pcols.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;


    @Override
    public Page<Review> getReviewsByProduct(Product product, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return reviewRepository.findByProduct(product, pageable);
    }

    @Override
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public ValidationResult validateReviewForm(ReviewFormDTO reviewForm) {
        ValidationResult result = new ValidationResult();

        if (reviewForm.comment() == null || reviewForm.comment().trim().isEmpty()) {
            result.addError("commentError", "Comment cannot be empty");
        } else if (reviewForm.comment().length() > 1000) {
            result.addError("commentError", "Comment cannot exceed 1000 characters");
        }

        if (reviewForm.rating() == null) {
            result.addError("ratingError", "Rating is required");
        } else if (reviewForm.rating() < 1 || reviewForm.rating() > 5) {
            result.addError("ratingError", "Rating must be between 1 and 5");
        }

        return result;
    }
}
