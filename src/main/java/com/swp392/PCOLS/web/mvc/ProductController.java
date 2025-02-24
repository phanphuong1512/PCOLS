package com.swp392.PCOLS.web.mvc;

import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/admin/product")
    public String getProductPage(Model model){
        List<Product> products = this.productService.getAllProduct();
        model.addAttribute("product1", products);
        System.out.println("check user" + products);
        return "admin/product/inventory";
    }

    @RequestMapping("/admin/product/create") // GET
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @RequestMapping(value = "/admin/product/create", method = RequestMethod.POST)
    public String createUserPage(Model model, @ModelAttribute("newProduct") Product product) {
        System.out.println("run here" + product);
        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }
}
