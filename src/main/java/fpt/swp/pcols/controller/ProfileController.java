package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.UpdateProfileDTO;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("")
    public String profilePage(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // get user from DB
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // create UpdateProfileDTO user data
            UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getAddress(),
                    null, // currentPassword
                    null, // newPassword
                    null  // confirmNewPassword
            );

            // add both user and updateProfileDTO to model
            model.addAttribute("user", user);
            model.addAttribute("updateProfileDTO", updateProfileDTO);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "User not found pls login");
            return "redirect:/auth/login";
        }

        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute("updateProfileDTO") UpdateProfileDTO updateProfileDTO,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            RedirectAttributes redirectAttributes,
            Principal principal) {

        boolean passwordChanged = false;
        try {
            String username = principal.getName();
            User existingUser = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existingUser.setUsername(updateProfileDTO.username());
            existingUser.setEmail(updateProfileDTO.email());
            existingUser.setPhone(updateProfileDTO.phone());
            existingUser.setAddress(updateProfileDTO.address());

            if (updateProfileDTO.newPassword() != null && !updateProfileDTO.newPassword().trim().isEmpty()) {
                userService.changePassword(username, updateProfileDTO.newPassword());
                passwordChanged = true;
            }

            if (avatarFile != null && !avatarFile.isEmpty()) {
                Path uploadPath = Paths.get(System.getProperty("user.home"), "pcols-uploads", "avatars");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                if (existingUser.getAvatar() != null && !existingUser.getAvatar().startsWith("http")) {
                    String oldFileName = Paths.get(existingUser.getAvatar()).getFileName().toString();
                    Path oldFilePath = uploadPath.resolve(oldFileName);
                    if (Files.exists(oldFilePath)) {
                        Files.delete(oldFilePath);
                    }
                }
                String fileName = existingUser.getId() + "_" + avatarFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                if (!Files.exists(filePath)) {
                    throw new IOException("Failed to save file: " + filePath);
                }
                existingUser.setAvatar("/uploads/avatars/" + fileName);
            }

            userService.updateUser(existingUser);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");

            if (passwordChanged) {
                SecurityContextHolder.clearContext();
                return "redirect:/auth/login?logout";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
        }
        return "redirect:/profile";
    }

}




