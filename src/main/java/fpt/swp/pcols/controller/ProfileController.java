package fpt.swp.pcols.controller;

import fpt.swp.pcols.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final UserService userService;

    // Handles GET requests to "/update-password" to render the password update form.
    @GetMapping({""})
    public String getProfilePage() {
        return "auth/profile";
    }

    // Handles POST requests to "/update-password" for updating user password securely.
    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 Principal principal,
                                 RedirectAttributes ra) {
        try {
            userService.updatePassword(principal.getName(), currentPassword, newPassword, confirmNewPassword);
            ra.addFlashAttribute("message", "Password updated successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile/";
    }
}



