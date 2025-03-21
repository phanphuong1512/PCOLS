package fpt.swp.pcols.controller;

import fpt.swp.pcols.entity.Authority;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.repository.AuthorityRepository;
import fpt.swp.pcols.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthorityRepository authorityRepository;


    @GetMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUser();
        model.addAttribute("users", users);
        //        System.out.println("check user" + users);
        return "admin/user/user-list";
    }

    @GetMapping("/admin/user/edit/{id}")
    public String getEditUserPage(Model model, @PathVariable long id) {
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
        userService.save(user);
        return "redirect:/admin/user";
    }
}
