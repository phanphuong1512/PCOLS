package com.swp392.PCOLS.controller;

import com.swp392.PCOLS.entity.Category;
import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.service.CategoryService;
import com.swp392.PCOLS.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    //khai bao phuc vu cho dependency injection
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    //lay het danh sach san pham
    @GetMapping("/admin/product")
    public String getInventoryPage(Model model){
        List<Product> products = this.productService.getAllProduct();
        model.addAttribute("products", products);
        System.out.println("check user" + products);
        return "admin/product/inventory";
    }

    //truy cap trang tao moi san pham
    //truyen vao object "newProduct" de tao moi
    //lay danh sach category
    @GetMapping("/admin/product/create") // GET
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/product/create";
    }

    //ham xu li luu san pham duoc tao moi
    @PostMapping(value = "/admin/product/create")
    public String createProduct(@ModelAttribute("newProduct") Product product,
                                @RequestParam("category.id") Long selectedCategoryId,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        // lay category tu DB
        //truyen category co san chu ko tao moi
        Category selectedCategory = categoryService.getCategoryById(selectedCategoryId);
        product.setCategory(selectedCategory);
        // upload anh
        if (!imageFile.isEmpty()) {
            uploadImage(product, imageFile);
        }
        // Save product
        productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }


    //lay chi tiet thong tin product theo id
    @GetMapping("/admin/product/detail/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id){
        Product product = this.productService.getProductById(id);
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }

    @PostMapping("/admin/product/detail/saveEdit")
    public String saveProductDetailEdit(@ModelAttribute Product product,
                                        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        System.out.println("Received file: " + (imageFile != null ? imageFile.getOriginalFilename() : "No file uploaded"));
        // Image Upload Logic
        if (imageFile != null && !imageFile.isEmpty()) {
            uploadImage(product, imageFile);
        } else {
            // Keep the existing image if no new one is uploaded
            product.setImage(productService.getProductById(product.getId()).getImage());
            System.out.println("Retained old image: " + product.getImage());
        }
        productService.handleSaveProduct(product);
        return "redirect:/admin/product/detail/" + product.getId();
    }

    private void uploadImage(@ModelAttribute Product product, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            String fileName = UUID.randomUUID().toString() + imageFile.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/uploads/" + fileName);
            Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            product.setImage("/uploads/" + fileName); // Store new image path in DB
            System.out.println("New image saved: " + product.getImage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error uploading image");
        }
    }

    @GetMapping("/ProductPage")
    public String getProductPage(Model model,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "brand", required = false) String brand,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "minPrice", required = false) Double minPrice,
                                 @RequestParam(value = "maxPrice", required = false) Double maxPrice) {
        List<Category> listCategories = categoryService.getAllCategories();
        List<String> listBrands = productService.getAllBrands();
        List<Product> products = productService.getFilteredProducts(brand, category, minPrice, maxPrice, sort);

        model.addAttribute("products", products);
        model.addAttribute("categories", category);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("brands", brand);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);
        return "products";
    }

}
