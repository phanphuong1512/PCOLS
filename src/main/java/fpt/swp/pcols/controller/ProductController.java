package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.DiscountDTO;
import fpt.swp.pcols.entity.*;
import fpt.swp.pcols.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

//    @GetMapping("/admin/dashboard")
//    public String getAdminDashboard(Model model, @RequestParam(value = "category", required = false) String category,
//                                    @RequestParam(value = "brand", required = false) String brand,
//                                    @RequestParam(value = "search", required = false) String search,
//                                    Authority authority) {
//        String categoryName = (category != null && !category.isEmpty()) ? category : null;
//        String brandName = (brand != null && !brand.isEmpty()) ? brand : null;
//        String searchTerm = (search != null && !search.isEmpty()) ? search : null;
//        String role = authority.getAuthority();
//        List<Product> products = productService.getFilteredProductsForAdmin(categoryName, brandName, searchTerm);
//        model.addAttribute("searchTerm", search);
//        model.addAttribute("categories", categoryService.findAll());
//        model.addAttribute("brands", brandService.findAll());
//        model.addAttribute("selectedCategory", category);
//        model.addAttribute("selectedBrand", brand);
//        model.addAttribute("role", role);
//        model.addAttribute("products", products);
//        System.out.println("check user" + products);
//        return "admin/admin-home";
//    }

    @GetMapping("/seller/product")
    public String getInventoryPage(Model model,
                                   @RequestParam(value = "category", required = false) String category,
                                   @RequestParam(value = "brand", required = false) String brand,
                                   @RequestParam(value = "search", required = false) String search) {
        String categoryName = (category != null && !category.isEmpty()) ? category : null;
        String brandName = (brand != null && !brand.isEmpty()) ? brand : null;
        String searchTerm = (search != null && !search.isEmpty()) ? search : null;
        List<Product> products = productService.getFilteredProductsForAdmin(categoryName, brandName, searchTerm);
        model.addAttribute("searchTerm", search);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("products", products);
        System.out.println("check user" + products);
        return "admin/product/inventory";
    }

    @GetMapping("/admin/product/ProductCreatePage") // GET
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        return "admin/product/create";
    }

    @PostMapping(value = "/admin/product/create")
    public String createProduct(@Valid @ModelAttribute("newProduct") Product product,
                                BindingResult bindingResult,
                                @RequestParam("category.id") Long selectedCategoryId,
                                @RequestParam("brand.id") Long selectedBrandId,
                                @RequestParam("imageFiles") List<MultipartFile> imageFiles,
                                Model model) {
        if (bindingResult.hasErrors()) {
            // Repopulate any necessary model attributes for the form:
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("brands", brandService.findAll());
            model.addAttribute("newProduct", product);
            // Return to the create form so that the errors are shown to the user
            return "admin/product/create";
        }
        productService.createProduct(product, selectedCategoryId, selectedBrandId, imageFiles);
        return "redirect:/seller/product";
    }

    //get product detail by id
    @GetMapping("/admin/product/detail/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id, @ModelAttribute List<MultipartFile> imageFiles) {
        Product product = this.productService.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        List<Category> categories = categoryService.findAll();
        List<Brand> brands = brandService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("product", product);
        model.addAttribute("imageFiles", imageFiles);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }

    @PostMapping("/admin/product/detail/saveEdit")
    public String saveProductDetailEdit(@Valid @ModelAttribute Product product,
                                        BindingResult bindingResult,
                                        @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                                        Model model) {

        if (bindingResult.hasErrors()) {
            // Repopulate supporting model attributes needed for the edit view (e.g., categories and brands)
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("brands", brandService.findAll());
            // Return to the edit form view, so that validation error messages can be displayed
            return "admin/product/detail";
        }
        productService.createProduct(product, product.getCategory().getId(), product.getBrand().getId(), imageFiles);
        productService.save(product);
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
        List<Category> listCategories = categoryService.findAll();
        List<Brand> listBrands = brandService.findAll();
        List<Product> products = productService.getFilteredProducts(brand, category, minPrice, maxPrice, sort);
        Map<Long, DiscountDTO> discountMap = discountService.getProductDiscounts(products);

        model.addAttribute("discountMap", discountMap);
        model.addAttribute("products", products);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("imageFiles", imageFiles);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);

        // Thêm map chứa rating cho từng product
        Map<Long, Double> averageRatingMap = new HashMap<>();
        for (Product product : products) {
            double avgRating = reviewService.calculateAverageRating(product.getId());
            averageRatingMap.put(product.getId(), avgRating);
        }
        System.out.println("Average Rating Map: " + averageRatingMap);

        model.addAttribute("averageRating", averageRatingMap);


        return "products";
    }

    @GetMapping("admin/product/disable/{id}")
    public String disableProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.disableProductById(id);
        redirectAttributes.addFlashAttribute("message", "Disable Successfully!");
        return "redirect:/seller/product"; // Redirect back to inventory after disable
    }
}