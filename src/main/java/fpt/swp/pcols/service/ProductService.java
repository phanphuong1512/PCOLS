package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface ProductService {

    void createProduct(Product product, Long categoryId, List<MultipartFile> imageFile);

    void handleSaveProduct(Product product);

    Product getProductById(long id);

    Collection<Product> getProductsByCategory(String categoryNam);

    List<Product> getProductsByCategoryWithImages(String category);

    List<Product> getRelatedProducts(Product product, int limit);

    List<String> getAllBrands();

    List<Product> getFilteredProducts(String brand, String category, Double minPrice, Double maxPrice, String sort);

    List<Product> getFilteredProductsForAdmin(String categoryName, String brandName, String searchTerm);

    void deleteImagesByProductId(Long id);

    void deleteProductById(Long id);

}
