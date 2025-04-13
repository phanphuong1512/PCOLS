package fpt.swp.pcols.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BillDTO(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Address is required") String address,
        @NotBlank(message = "City is required") String city,
        @NotBlank(message = "Country is required") String country,
        @NotBlank(message = "Zip Code is required") String zipCode,
        @NotBlank(message = "Phone is required") String phone,
        String shipping,
        String payment
) {
}
