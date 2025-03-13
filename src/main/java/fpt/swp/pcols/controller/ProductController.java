package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.service.CategoryService;
import fpt.swp.pcols.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;


    @GetMapping("/admin/product")
    public String getInventoryPage(Model model) {
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

    @PostMapping(value = "/admin/product/create")
    public String createProduct(@ModelAttribute("newProduct") Product product,
                                @RequestParam("category.id") Long selectedCategoryId,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        productService.createProduct(product, selectedCategoryId, imageFile);
        return "redirect:/admin/product";
    }

    //lay chi tiet thong tin product theo id
    @GetMapping("/admin/product/detail/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
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

    @GetMapping("/products")
    public String getProductPage(Model model,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "page", defaultValue = "1") int page) {
        int PAGE_SIZE = 12;
        Page<Product> productPage;
        if ("asc".equals(sort)) {
            productPage = productService.getAllProductsSortedByPrice(page, PAGE_SIZE, Sort.Direction.ASC);
        } else if ("desc".equals(sort)) {
            productPage = productService.getAllProductsSortedByPrice(page, PAGE_SIZE, Sort.Direction.DESC);
        } else {
            productPage = productService.getAllProductsPaginated(page, PAGE_SIZE); // Default: No sorting
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("sortByPrice", sort);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "products";
    }
}
