package fpt.swp.pcols.service;

import fpt.swp.pcols.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface ProductService {


    void createProduct(Product product, Long categoryId, MultipartFile imageFile);

    List<Product> getAllProduct();

    void handleSaveProduct(Product product);

    Product getProductById(long id);

    Page<Product> getAllProductsSortedByPrice(int page, int size, Sort.Direction direction);

    List<Product> getAllProducts();

    Page<Product> getAllProductsPaginated(int page, int size);

    Collection<Product> getProductsByCategory(String categoryNam);

    public List<Product> getProductsByCategoryWithImages(String category);
}
