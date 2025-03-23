package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.LoginDTO;
import fpt.swp.pcols.dto.RegisterDTO;
import fpt.swp.pcols.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @PostMapping("/loginRE")
    public ResponseEntity<?> login(@ModelAttribute LoginDTO loginDTO) {
        return new ResponseEntity<>(userService.login(loginDTO), HttpStatus.OK);
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return "/auth/otp";
    }

    @GetMapping("/verify-account")
    public String verifyAccount(@RequestParam String email, @RequestParam String otp) {
        userService.verifyAccount(email, otp);
        return "redirect:/auth/login";
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<?> regenerateOtp(@RequestParam String email) {
        userService.regenerateOtp(email);
        return new ResponseEntity<>("regenerated otp", HttpStatus.OK);
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, RedirectAttributes ra) {
        userService.forgotPassword(email);
        ra.addFlashAttribute("message", "Email sent for the verification");
        return "redirect:/auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword, RedirectAttributes ra) {
        userService.resetPassword(email, newPassword);
        ra.addFlashAttribute("message", "Password reset successfully. Please login again.");
        return "redirect:/auth/login";
    }
}


