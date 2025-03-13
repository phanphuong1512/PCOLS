package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Product;
import fpt.swp.pcols.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping({"/", "/home"})
    public String getHomePage(Model model) {
        List<Product> listProducts = productService.getAllProducts();
        model.addAttribute("products", listProducts);
        return "home";
    }

    @GetMapping("/product-page")
    public String getProductPage() {
        //        List<Product> products = this.productService.getAllProduct();
        //        model.addAttribute("products", products);
        return "product-detail";
    }
}