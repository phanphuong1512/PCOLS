package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Image;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.repository.CategoryRepository;
import fpt.swp.pcols.repository.ImageRepository;
import fpt.swp.pcols.repository.ProductRepository;
import fpt.swp.pcols.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public void createProduct(Product product, Long categoryId, List<MultipartFile> imageFiles) {
        // get category from DB
        Category selectedCategory = categoryService.getCategoryById(categoryId);
        product.setCategory(selectedCategory);
        product.getImages().clear(); // Clear all images
        productRepository.save(product); // Save product to DB

        // handle upload file if not null
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    try {
                        String fileName = imageFile.getOriginalFilename();
                        Path imagePath = Paths.get("src/main/resources/static/uploads/" + fileName);
                        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                        Image image = new Image();
                        image.setUrl("/uploads/" + fileName);
                        image.setProduct(product);
                        imageRepository.save(image); // Save image to DB

                    } catch (IOException e) {
                        throw new RuntimeException("Error uploading image", e);
                    }
                }
            }
        }
    }

    @Override
    public void handleSaveProduct(Product product) {
        this.productRepository.save(product);
    }

    @Override
    public Product getProductById(long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<Product> getProductsByCategoryWithImages(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));
        return productRepository.findByCategoryWithImages(category);
    }

    public List<Product> getProductsByCategory(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));
        return productRepository.findByCategoryWithImages(category);
    }

    @Override
    public List<Product> getRelatedProducts(Product product, int limit) {
        return getProductsByCategory(product.getCategory().getName())
                .stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllBrands() {
        return this.productRepository.findAllBrands();
    }

    @Override
    public List<Product> getFilteredProducts(String brand, String category, Double minPrice, Double maxPrice, String sort) {
        return this.productRepository.findFilteredProducts(brand, category, minPrice, maxPrice, sort);
    }

    @Override
    public List<Product> getFilteredProductsForAdmin(String categoryName, String brandName, String searchTerm) {
        return productRepository.findProducts(categoryName, brandName, searchTerm);
    }

    @Override
    public void deleteImagesByProductId(Long id) {
        this.imageRepository.deleteByProductId(id);
    }

    @Override
    public void deleteProductById(Long id) {
        this.productRepository.deleteById(id);
    }

}
