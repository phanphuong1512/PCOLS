package fpt.swp.pcols.dto;

public record UpdateProfileDTO(
        Long id,
        String username,
        String email,
        String phone,
        String address,
        String currentPassword,
        String newPassword,
        String confirmNewPassword
) {
}