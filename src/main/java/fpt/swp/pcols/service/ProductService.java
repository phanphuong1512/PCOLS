package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    void createProduct(Product product, Long categoryId, Long brandId, List<MultipartFile> imageFile);

    void save(Product product);

    Optional<Product> findById(long id);

    Collection<Product> getProductsByCategory(String categoryName);

    List<Product> getProductsByCategoryWithImages(String category);

    List<Product> getRelatedProducts(Product product, int limit);

    List<Product> getFilteredProducts(String brand, String category, Double minPrice, Double maxPrice, String sort);

    List<Product> getFilteredProductsForAdmin(String categoryName, String brandName, String searchTerm);

    void deleteImagesByProductId(Long id);

    List<Product> getAllProducts();

    void disableProductById(Long id);
}
