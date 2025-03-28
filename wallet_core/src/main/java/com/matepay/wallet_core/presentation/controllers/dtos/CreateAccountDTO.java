package com.matepay.wallet_core.presentation.controllers.dtos;

import com.matepay.wallet_core.presentation.controllers.validations.annotations.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateAccountDTO(
        @NotEmpty
        @ValidUUID
        String clientId
) {
}
