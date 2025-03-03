package com.swp392.PCOLS.web.mvc;

import com.swp392.PCOLS.dto.LoginDTO;
import com.swp392.PCOLS.dto.RegisterDTO;
import com.swp392.PCOLS.repository.UserRepository;
import com.swp392.PCOLS.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/loginRE")
    public ResponseEntity<String> login(@ModelAttribute LoginDTO loginDTO) {
        return new ResponseEntity<>(userService.login(loginDTO), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@ModelAttribute RegisterDTO registerDTO) {
        return new ResponseEntity<>(userService.register(registerDTO), HttpStatus.OK);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }


}


