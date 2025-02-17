package com.swp392.PCOLS.web.mvc;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.entity.User;

@Controller
@RequestMapping("/auth")
public class LoginController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               RedirectAttributes redirectAttributes) {
        // Kiểm tra xem username đã tồn tại hay chưa
        if (userRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addAttribute("error", "Username đã tồn tại");
            return "redirect:/auth/login";
        }
        // Tạo user mới và mã hóa mật khẩu
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
        redirectAttributes.addAttribute("success", "Sign up success, please login");
        return "redirect:/auth/login";
    }
}


