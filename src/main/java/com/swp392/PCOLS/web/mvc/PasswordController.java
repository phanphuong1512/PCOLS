package com.swp392.PCOLS.web.mvc;

import com.swp392.PCOLS.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class PasswordController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    // Handles GET requests to "/update-password" to render the password update form.
    @GetMapping("/profile")
    public String updatePasswordForm() {
        return "auth/profile";
    }

    // Handles POST requests to "/update-password" for updating user password securely.
    @PostMapping("/profile")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 Principal principal, Model model) {
        try {
            // Validates and updates the password for the authenticated user.
            if (!newPassword.equals(confirmNewPassword)) {
                model.addAttribute("error", "New passwords do not match");
                return "auth/profile";
            }

            userServiceImpl.updatePassword(principal.getName(), currentPassword, newPassword);
            model.addAttribute("message", "Password updated successfully");
        } catch (Exception e) {
            // Handles exceptions during password update process.
            model.addAttribute("error", e.getMessage());
        }
        return "auth/profile";
    }
}

