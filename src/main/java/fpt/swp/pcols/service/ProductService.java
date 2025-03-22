package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface ProductService {


    void createProduct(Product product, Long categoryId, List<MultipartFile> imageFile);

    List<Product> getAllProduct();

    void handleSaveProduct(Product product);

    Product getProductById(long id);

    Page<Product> getAllProductsSortedByPrice(int page, int size, Sort.Direction direction);

    List<Product> getAllProducts();

    Collection<Product> getProductsByCategory(String categoryNam);

    public List<Product> getProductsByCategoryWithImages(String category);

    List<String> getAllBrands();

    List<Product> getFilteredProducts(String brand, String category, Double minPrice, Double maxPrice, String sort);

    List<Product> getFilteredProductsForAdmin(String categoryName, String brandName, String searchTerm);

    void deleteImagesByProductId(Long id);

    void deleteProductById(Long id);
}
