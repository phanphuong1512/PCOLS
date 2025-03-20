package fpt.swp.pcols.repository;

import fpt.swp.pcols.entity.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long productId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Image i WHERE i.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}