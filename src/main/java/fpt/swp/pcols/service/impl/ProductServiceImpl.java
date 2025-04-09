package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Brand;
import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Image;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.repository.CategoryRepository;
import fpt.swp.pcols.repository.ImageRepository;
import fpt.swp.pcols.repository.ProductRepository;
import fpt.swp.pcols.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${upload.dir}")
    private String uploadDir;

    private final ProductRepository productRepository;
    private final CategoryServiceImpl categoryService;
    private final BrandServiceImpl brandService;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public void createProduct(Product product, Long categoryId, Long brandId, List<MultipartFile> imageFiles) {
        // get category from DB
        Category selectedCategory = categoryService.getCategoryById(categoryId);
        Brand selectedBrand = brandService.getBrandById(categoryId);

        boolean hasNewImages = imageFiles != null && imageFiles.stream().anyMatch(file -> !file.isEmpty());
        product.setCategory(selectedCategory);
        product.setBrand(selectedBrand);

        if (hasNewImages) {
            // Remove existing images only if new ones are uploaded
            product.getImages().clear();
        }

        productRepository.save(product); // Save product to DB

        // handle upload file if not null
        if (hasNewImages) {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to create upload directory", e);
                }
            }
            // Allowed MIME types for images
            List<String> allowedTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/gif");


            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    // Validate file type
                    if (!allowedTypes.contains(imageFile.getContentType())) {
                        throw new RuntimeException("Invalid file type! Only images are allowed.");
                    }
                    try {
                        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                        Path imagePath = uploadPath.resolve(fileName);
                        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                        // Save image URL to database (pseudo-code)
                        Image image = new Image();
                        image.setUrl("/" + uploadDir + "/" + fileName);
                        image.setProduct(product);
                        imageRepository.save(image);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
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

//    @Override
//    public List<String> getAllBrands() {
//        return this.productRepository.findAllBrands();
//    }

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
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public void disableProductById(Long id) {
        this.productRepository.disableProductById(id);
    }

}
