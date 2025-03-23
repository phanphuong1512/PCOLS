package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.UpdatePasswordDTO;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("")
    public String profilePage(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // Lấy user từ DB theo username
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Thêm user vào model
            model.addAttribute("user", user);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng. Vui lòng đăng nhập lại.");
            return "redirect:/auth/login";
        }

        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute("user") User user,
            @ModelAttribute UpdatePasswordDTO passwordDTO,
            RedirectAttributes redirectAttributes) {

        System.out.println("User ID: " + user.getId());
        System.out.println("User data: " + user);
        try {
            // Cập nhật thông tin từ tab "General"dddddđ
            userService.updateUser(user);

            // Xử lý thay đổi mật khẩu từ tab "Change Password"
            if (passwordDTO.currentPassword() != null && !passwordDTO.currentPassword().isEmpty()) {
                // Kiểm tra mật khẩu hiện tại có đúng không
                if (!userService.checkPassword(user.getUsername(), passwordDTO.currentPassword())) {
                    redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng");
                    return "redirect:/profile";
                }

                // Kiểm tra newPassword và confirmNewPassword có khớp không (validation cơ bản)
                if (passwordDTO.newPassword() == null || passwordDTO.newPassword().isEmpty()) {
                    redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không được để trống");
                    return "redirect:/profile";
                }
                if (!passwordDTO.newPassword().equals(passwordDTO.confirmNewPassword())) {
                    redirectAttributes.addFlashAttribute("error", "Mật khẩu mới và xác nhận không khớp");
                    return "redirect:/profile";
                }

                // Đổi mật khẩu
                userService.changePassword(user.getUsername(), passwordDTO.newPassword());
            }

            redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật hồ sơ: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}

