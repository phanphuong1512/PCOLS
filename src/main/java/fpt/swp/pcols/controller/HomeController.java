package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.service.CategoryService;
import fpt.swp.pcols.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        List<Product> gearProducts = productService.getProductsWithFirstImageByCategory("Gear", 3);
        List<Product> monitorProducts = productService.getProductsWithFirstImageByCategory("Monitor", 3);
        List<Product> pcProducts = productService.getProductsWithFirstImageByCategory("PC", 3);

        model.addAttribute("gearProducts", gearProducts);
        model.addAttribute("monitorProducts", monitorProducts);
        model.addAttribute("pcProducts", pcProducts);

        // New code: Load all categories dynamically and prepare a map of category name to up to 3 products
        List<Category> allCategories = categoryService.getAllCategories();
        System.out.println("All Categories: " + allCategories);

        Map<String, List<Product>> categoryProductsMap = new HashMap<>();
        for (Category category : allCategories) {
            List<Product> products = productService.getProductsByCategory(category.getName())
                    .stream().limit(4)
                    .collect(Collectors.toList());
            categoryProductsMap.put(category.getName(), products);
        }
        model.addAttribute("categoryProductsMap", categoryProductsMap);
        System.out.println("categoryProductsMap = " + categoryProductsMap);

        return "home";
    }

    @GetMapping("/product-detail")
    public String getProductDetail(@RequestParam("id") Long productId, Model model) {
        Product product = productService.getProductById(productId);
        Category category = product.getCategory();
        model.addAttribute("product", product);
        model.addAttribute("category", category);

        // Lấy danh sách sản phẩm cùng category, loại bỏ sản phẩm đang xem, giới hạn 4 sản phẩm
        List<Product> relatedProducts = productService.getProductsByCategory(category.getName())
                .stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(4)
                .collect(Collectors.toList());
        model.addAttribute("relatedProducts", relatedProducts);

        return "product-detail";
    }
}


