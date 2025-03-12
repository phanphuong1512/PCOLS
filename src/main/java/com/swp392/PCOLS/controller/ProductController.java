package com.swp392.PCOLS.controller;

import com.swp392.PCOLS.entity.Category;
import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.service.CategoryService;
import com.swp392.PCOLS.service.ProductService;
import org.springframework.data.domain.Page;
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
            try {
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path imagePath = Paths.get("src/main/resources/static/uploads/" + fileName);
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
                product.setImage("/uploads/" + fileName); // Store relative path in DB
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public String saveProductDetailEdit(@ModelAttribute Product product) {
        productService.handleSaveProduct(product);
        return "redirect:/admin/product/detail/" + product.getId();
    }

    @GetMapping("/products/ProductPage ")
    public String getProductPage(@RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 Model model) {
        Page<Product> productPage;

        if (categoryId != null && categoryId > 0) {
            productPage = productService.getProductsByCategoryPaginated(categoryId, page, size);
        } else {
            productPage = productService.getAllProductsPaginated(page, size);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("size", size);
        model.addAttribute("products", productPage);
        model.addAttribute("categories", categoryService.getAllCategory());
        return "products";
    }
}
