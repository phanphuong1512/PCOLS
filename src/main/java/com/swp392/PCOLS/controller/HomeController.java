package com.swp392.PCOLS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/product-page")
    public String getProductPage() {
        //        List<Product> products = this.productService.getAllProduct();
        //        model.addAttribute("products", products);
        return "product-detail";
    }
}