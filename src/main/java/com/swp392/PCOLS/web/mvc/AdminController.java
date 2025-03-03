package com.swp392.PCOLS.web.mvc;

import com.swp392.PCOLS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    //    @GetMapping("/users")
    //    public String listUsers(@RequestParam(value = "keyword", required = false) String keyword,
    //                            @RequestParam(value = "sortBy", defaultValue = "createdDate") String sortBy,
    //                            Model model) {
    //        List<User> users = userService.getAllUsers(keyword, sortBy);
    //        model.addAttribute("users", users);
    //        return "admin/user-list";
    //    }

    //    @GetMapping("/users/{id}")
    //    public String userDetail(@PathVariable Long id, Model model) {
    //        User user = userService.getUserById(id);
    //        model.addAttribute("user", user);
    //        return "admin/user-detail";
    //    }
}

