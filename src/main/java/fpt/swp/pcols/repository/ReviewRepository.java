package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByProduct(Product product, Pageable pageable);

    List<Review> findByProductId(Long productId);

}