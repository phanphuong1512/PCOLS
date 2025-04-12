package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.DiscountDTO;
import fpt.swp.pcols.entity.Category;
import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.service.CategoryService;
import fpt.swp.pcols.service.DiscountService;
import fpt.swp.pcols.service.ProductService;
import fpt.swp.pcols.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final DiscountService discountService;


    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        List<Product> gearProducts = productService.getProductsByCategoryWithImages("Gear").stream().limit(3).collect(Collectors.toList());
        List<Product> monitorProducts = productService.getProductsByCategoryWithImages("Monitor").stream().limit(3).collect(Collectors.toList());
        List<Product> pcProducts = productService.getProductsByCategoryWithImages("PC").stream().limit(3).collect(Collectors.toList());

        // Gộp tất cả sản phẩm
        List<Product> allProducts = Stream.of(
                gearProducts,
                monitorProducts,
                pcProducts
        ).flatMap(List::stream).toList();
        Map<Long, DiscountDTO> discountMap = discountService.getProductDiscounts(allProducts);
        model.addAttribute("discountMap", discountMap);

        model.addAttribute("gearProducts", gearProducts);
        model.addAttribute("monitorProducts", monitorProducts);
        model.addAttribute("pcProducts", pcProducts);

        Map<Long, Double> averageRatingMap = new HashMap<>();
        for (Product product : gearProducts) {
            double avgRating = reviewService.calculateAverageRating(product.getId());
            averageRatingMap.put(product.getId(), avgRating);
        }
        for (Product product : monitorProducts) {
            double avgRating = reviewService.calculateAverageRating(product.getId());
            averageRatingMap.put(product.getId(), avgRating);
        }
        for (Product product : pcProducts) {
            double avgRating = reviewService.calculateAverageRating(product.getId());
            averageRatingMap.put(product.getId(), avgRating);
        }

        model.addAttribute("averageRating", averageRatingMap);


        // New code: Load all categories dynamically and prepare a map of category name to up to 3 products
        List<Category> allCategories = categoryService.findAll();
        System.out.println("All Categories: " + allCategories);

        Map<String, List<Product>> categoryProductsMap = new HashMap<>();
        for (Category category : allCategories) {
            List<Product> products = productService.getProductsByCategory(category.getName()).stream().limit(4).collect(Collectors.toList());
            categoryProductsMap.put(category.getName(), products);
        }
        model.addAttribute("categoryProductsMap", categoryProductsMap);
        System.out.println("categoryProductsMap = " + categoryProductsMap);

        return "home";
    }

    @GetMapping("/chatbot")
    public String chatBot() {
        return "/fragments/chatbot";
    }

}


