package com.swp392.PCOLS.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth")
public class LoginController {

//    @RequestMapping(name = "/login", method = RequestMethod.GET)
    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

}


