package fpt.swp.pcols.controller;

import fpt.swp.pcols.dto.UpdateProfileDTO;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/avatars/";
    private static final String URL_PREFIX = "/uploads/avatars/";

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @GetMapping("")
    public String profilePage(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // Lấy user từ DB theo username
            User user = userService.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Tạo UpdateProfileDTO từ thông tin user
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

            // Thêm cả user và updateProfileDTO vào model
            model.addAttribute("user", user); // Để hiển thị avatar và emailVerified
            model.addAttribute("updateProfileDTO", updateProfileDTO);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng. Vui lòng đăng nhập lại.");
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

        try {
            // Lấy user hiện tại từ database
            String username = principal.getName();
            User existingUser = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Cập nhật thông tin user từ DTO
            existingUser.setUsername(updateProfileDTO.username());
            existingUser.setEmail(updateProfileDTO.email());
            existingUser.setPhone(updateProfileDTO.phone());
            existingUser.setAddress(updateProfileDTO.address());

            // Xử lý upload avatar nếu có file
            if (avatarFile != null && !avatarFile.isEmpty()) {
                // Đường dẫn tuyệt đối đến thư mục lưu file
                Path uploadPath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIR);

                // Tạo thư mục nếu chưa tồn tại
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    logger.debug("Đã tạo thư mục: {}", uploadPath);
                }

                // Xóa file avatar cũ nếu có
                if (existingUser.getAvatar() != null && !existingUser.getAvatar().startsWith("http")) {
                    Path oldFilePath = Paths.get(System.getProperty("user.dir"), "src/main/resources/static", existingUser.getAvatar());
                    if (Files.exists(oldFilePath)) {
                        Files.delete(oldFilePath);
                        logger.debug("Đã xóa avatar cũ: {}", oldFilePath);
                    }
                }

                // Lưu file mới
                String fileName = existingUser.getId() + "_" + avatarFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // Sử dụng Files.copy thay vì transferTo để đồng bộ với code cũ
                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Kiểm tra xem file có thực sự được lưu không
                if (!Files.exists(filePath)) {
                    throw new IOException("Không thể lưu file: " + filePath);
                }

                // Cập nhật đường dẫn avatar trong user
                existingUser.setAvatar(URL_PREFIX + fileName);
                logger.debug("Đã lưu avatar: {}", filePath);
            }

            // Lưu thông tin user vào database
            userService.updateUser(existingUser);
            redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật hồ sơ: ", e);
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật hồ sơ: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}




