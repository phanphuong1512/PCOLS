package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Brand;
import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Discount;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ReviewService reviewService;
    private final DiscountService discountService;

    @GetMapping("/admin/dashboard")
    public String getAdminDashboard(Model model, @RequestParam(value = "category", required = false) String category, @RequestParam(value = "brand", required = false) String brand, @RequestParam(value = "search", required = false) String search) {
        String categoryName = (category != null && !category.isEmpty()) ? category : null;
        String brandName = (brand != null && !brand.isEmpty()) ? brand : null;
        String searchTerm = (search != null && !search.isEmpty()) ? search : null;
        List<Product> products = productService.getFilteredProductsForAdmin(categoryName, brandName, searchTerm);
        model.addAttribute("searchTerm", search);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("products", products);
        System.out.println("check user" + products);
        return "admin/admin-home";
    }

    @GetMapping("/admin/product")
    public String getInventoryPage(Model model,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "brand", required = false) String brand,
                                   @RequestParam(value = "search", required = false) String search) {
        String categoryName = (category != null && !category.isEmpty()) ? category : null;
        String brandName = (brand != null && !brand.isEmpty()) ? brand : null;
        String searchTerm = (search != null && !search.isEmpty()) ? search : null;
        List<Product> products = productService.getFilteredProductsForAdmin(categoryName, brandName, searchTerm);
        model.addAttribute("searchTerm", search);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("products", products);
        System.out.println("check user" + products);
        return "admin/product/inventory";
    }

    @GetMapping("/admin/product/ProductCreatePage") // GET
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/product/create";
    }

    @PostMapping(value = "/admin/product/create")
    public String createProduct(@ModelAttribute("newProduct") Product product,
                                @RequestParam("category.id") Long selectedCategoryId,
                                @RequestParam("imageFiles") List<MultipartFile> imageFiles) {
        productService.createProduct(product, selectedCategoryId, imageFiles);
        return "redirect:/admin/product";
    }

    //get product detail by id
    @GetMapping("/admin/product/detail/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id, @ModelAttribute List<MultipartFile> imageFiles) {
        Product product = this.productService.getProductById(id);
        List<Category> categories = categoryService.getAllCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        model.addAttribute("imageFiles", imageFiles);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }

    @PostMapping("/admin/product/detail/saveEdit")
    public String saveProductDetailEdit(@ModelAttribute Product product,
                                        @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
        // Delete old images before saving new ones
        productService.deleteImagesByProductId(product.getId());
        productService.createProduct(product, product.getCategory().getId(), imageFiles);
        productService.handleSaveProduct(product);
        return "redirect:/admin/product/detail/" + product.getId();
    }

    @GetMapping("/products")
    public String getProductPage(Model model,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "brand", required = false) String brand,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                                 @RequestParam(value = "minPrice", required = false) Double minPrice,
                                 @RequestParam(value = "maxPrice", required = false) Double maxPrice) {
        List<Category> listCategories = categoryService.getAllCategories();
        List<Brand> listBrands = brandService.getAllBrands();
        List<Product> products = productService.getFilteredProducts(brand, category, minPrice, maxPrice, sort);

        model.addAttribute("products", products);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("imageFiles", imageFiles);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);




        return "products";
    }

    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProductById(id);
        redirectAttributes.addFlashAttribute("message", "Delete Successfully!");
        return "redirect:/admin/product"; // Redirect back to inventory after deletion
    }
}