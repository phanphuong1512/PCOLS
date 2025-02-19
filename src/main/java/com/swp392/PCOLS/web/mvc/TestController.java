package com.swp392.PCOLS.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String showTestPage() {
        // Trả về tên của view, Thymeleaf sẽ tìm file test.html trong folder templates
        return "test-auth";
    }
}
