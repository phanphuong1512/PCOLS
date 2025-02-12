package com.swp392.PCOLS.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home", "/dashboard"})
    public String getHomePage() {
        return "home";
    }

}
