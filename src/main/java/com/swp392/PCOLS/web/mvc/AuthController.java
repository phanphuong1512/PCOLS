package com.swp392.PCOLS.web.mvc;

import com.swp392.PCOLS.dto.RegisterDTO;
import com.swp392.PCOLS.entity.User;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

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

    @PostMapping("/registerv2")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        return new ResponseEntity<>(userService.registerv2(registerDTO), HttpStatus.OK);
    }

    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp){
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }
}


