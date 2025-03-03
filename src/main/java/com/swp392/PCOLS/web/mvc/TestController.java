package com.swp392.PCOLS.web.mvc;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@PermitAll
public class TestController {

    @GetMapping("/demo")
    public ResponseEntity<?> test() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "phuong");
        data.put("age", 22);
        data.put("address", "Ha Noi");

        return ResponseEntity.ok(data);
    }
    public record StudentDTO (String name, int age, String address) {
    }
}
