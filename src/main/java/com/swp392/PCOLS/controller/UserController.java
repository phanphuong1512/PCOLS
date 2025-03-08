package com.swp392.PCOLS.controller;

import com.swp392.PCOLS.entity.Authority;
import com.swp392.PCOLS.entity.Product;
import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.AuthorityRepository;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserController(UserService userService, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("/admin/user")
    public String getUserPage(Model model){
        List<User> users = this.userService.getAllUser();
        model.addAttribute("users", users);
//        System.out.println("check user" + users);
        return "admin/user/user-list";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String getEditUserPage(Model model, @PathVariable long id){
        User user = this.userService.getUserById(id);
        List<Authority> authorities = this.authorityRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("authorities", authorities);
        model.addAttribute("id", id);

        return "admin/user/edit";
    }

    @PostMapping("/admin/user/update")
    public String updateUser(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/user/edit";
        }
        userService.updateUser(user);
        return "redirect:/admin/user";
    }
}
