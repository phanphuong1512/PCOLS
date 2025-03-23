package fpt.swp.pcols.dto;

public record UpdatePasswordDTO(
        String currentPassword,
        String newPassword,
        String confirmNewPassword
) {
}