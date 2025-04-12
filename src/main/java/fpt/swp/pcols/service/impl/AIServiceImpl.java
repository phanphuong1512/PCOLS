package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Discount;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.Review;
import fpt.swp.pcols.repository.DiscountRepository;
import fpt.swp.pcols.repository.ProductRepository;
import fpt.swp.pcols.repository.ReviewRepository;
import fpt.swp.pcols.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public AIServiceImpl(ProductRepository productRepository,
                         DiscountRepository discountRepository,
                         ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.reviewRepository = reviewRepository;
    }

    /**
     * Lấy danh sách sản phẩm có discount đang hoạt động.
     * Ví dụ: lấy các sản phẩm có discount record active (isActive = true, thời gian hiện tại trong khoảng startDate – endDate)
     */
    @Override
    public List<Product> findDiscountedProducts() {
        LocalDateTime now = LocalDateTime.now();
        // Lấy các discount đang active
        List<Discount> activeDiscounts = discountRepository
                .findByIsActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThan(now, now);
        // Lấy các product id từ các discount có liên kết tới sản phẩm
        Set<Long> productIds = activeDiscounts.stream()
                .filter(discount -> discount.getProduct() != null)
                .map(discount -> discount.getProduct().getId())
                .collect(Collectors.toSet());
        if (productIds.isEmpty()) {
            return Collections.emptyList();
        }
        // Lấy tất cả các sản phẩm có ID trong tập productIds
        List<Product> discountedProducts = productRepository.findAllByIdIn(productIds);
        // Giới hạn trả về 5 sản phẩm
        return discountedProducts.stream().limit(5).collect(Collectors.toList());
    }

    /**
     * Tính điểm trung bình của các review cho mỗi sản phẩm và trả về 5 sản phẩm có điểm trung bình cao nhất.
     * Nếu sản phẩm không có review, sản phẩm đó không được tính.
     */
    @Override
    public List<Product> findTopRatedProducts() {
        List<Product> allProducts = productRepository.findAll();
        Map<Product, Double> productAvgRating = new HashMap<>();
        for (Product product : allProducts) {
            List<Review> reviews = product.getReviews();
            if (reviews != null && !reviews.isEmpty()) {
                double avg = reviews.stream()
                        .mapToInt(Review::getRating)
                        .average()
                        .orElse(0.0);
                productAvgRating.put(product, avg);
            }
        }
        // Sắp xếp theo điểm trung bình giảm dần và giới hạn 5 sản phẩm đầu tiên
        return productAvgRating.entrySet().stream()
                .sorted(Map.Entry.<Product, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
