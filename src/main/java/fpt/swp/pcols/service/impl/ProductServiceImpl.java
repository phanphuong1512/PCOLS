package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.repository.ProductRepository;
import fpt.swp.pcols.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;


    @Override
    public void createProduct(Product product, Long categoryId, MultipartFile imageFile) {
        // get category from DB
        Category selectedCategory = categoryService.getCategoryById(categoryId);
        product.setCategory(selectedCategory);

        // handle upload file if not null
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                Path imagePath = Paths.get("src/main/resources/static/uploads/" + fileName);
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                product.setImage("/uploads/" + fileName); // save relative path to DB
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image", e);
            }
        }
        productRepository.save(product);
    }

    @Override
    public List<Product> getAllProduct() {
        return this.productRepository.findAll();
    }

    @Override
    public void handleSaveProduct(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public Product getProductById(long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllProductsSortedByPrice(int page, int pageSize, Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(direction, "price"));
        return productRepository.findAll(pageable);
    }

    @Override
    public Collection<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategory_Name(categoryName);
    }
}
