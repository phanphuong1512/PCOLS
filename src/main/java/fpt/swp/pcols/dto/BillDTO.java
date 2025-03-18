package fpt.swp.pcols.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BillDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank String address,
        @NotBlank String city,
        @NotBlank String country,
        @NotBlank String zipCode,
        @NotBlank String phone,
        @NotBlank String shipping, // FREE, STANDARD
        @NotBlank String payment   // BANK, PAYPAL
) {
}
