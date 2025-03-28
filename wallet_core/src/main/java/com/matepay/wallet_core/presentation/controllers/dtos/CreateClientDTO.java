package com.matepay.wallet_core.presentation.controllers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateClientDTO(
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email
) {
}
