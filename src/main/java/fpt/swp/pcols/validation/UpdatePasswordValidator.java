package fpt.swp.pcols.validation;

import fpt.swp.pcols.dto.UpdatePasswordDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdatePasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdatePasswordDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdatePasswordDTO dto = (UpdatePasswordDTO) target;

        // Kiểm tra currentPassword
        if (dto.currentPassword() == null || dto.currentPassword().trim().isEmpty()) {
            errors.rejectValue("currentPassword", "NotBlank", "Mật khẩu hiện tại là bắt buộc");
        }

        // Kiểm tra newPassword
        if (dto.newPassword() == null || dto.newPassword().trim().isEmpty()) {
            errors.rejectValue("newPassword", "NotBlank", "Mật khẩu mới là bắt buộc");
        } else if (dto.newPassword().length() < 6) {
            errors.rejectValue("newPassword", "Size", "Mật khẩu mới phải có ít nhất 6 ký tự");
        }

        // Kiểm tra confirmNewPassword
        if (dto.confirmNewPassword() == null || dto.confirmNewPassword().trim().isEmpty()) {
            errors.rejectValue("confirmNewPassword", "NotBlank", "Xác nhận mật khẩu mới là bắt buộc");
        }

        // Kiểm tra newPassword và confirmNewPassword có khớp không
        if (dto.newPassword() != null && dto.confirmNewPassword() != null && !dto.newPassword().equals(dto.confirmNewPassword())) {
            errors.rejectValue("confirmNewPassword", "Match", "Mật khẩu mới và xác nhận không khớp");
        }
    }
}