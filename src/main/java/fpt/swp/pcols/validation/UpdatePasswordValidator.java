package fpt.swp.pcols.validation;

import fpt.swp.pcols.dto.UpdatePasswordDTO;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdatePasswordValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UpdatePasswordDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UpdatePasswordDTO dto = (UpdatePasswordDTO) target;

        // Validate current password
        if (dto.currentPassword() == null || dto.currentPassword().trim().isEmpty()) {
            errors.rejectValue("currentPassword", "NotBlank", "Current password is required");
        }

        // Validate new password
        if (dto.newPassword() == null || dto.newPassword().trim().isEmpty()) {
            errors.rejectValue("newPassword", "NotBlank", "New password is required");
        } else if (dto.newPassword().length() < 6) {
            errors.rejectValue("newPassword", "Size", "New password must be at least 6 characters");
        }

        // Validate confirmation of new password
        if (dto.confirmNewPassword() == null || dto.confirmNewPassword().trim().isEmpty()) {
            errors.rejectValue("confirmNewPassword", "NotBlank", "Confirmation of new password is required");
        }

        // Check if new password and confirmation match
        if (dto.newPassword() != null && dto.confirmNewPassword() != null
                && !dto.newPassword().equals(dto.confirmNewPassword())) {
            errors.rejectValue("confirmNewPassword", "Match", "New password and confirmation do not match");
        }
    }
}
