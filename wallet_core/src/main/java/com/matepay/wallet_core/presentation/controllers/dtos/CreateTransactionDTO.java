package com.matepay.wallet_core.presentation.controllers.dtos;

import com.matepay.wallet_core.presentation.controllers.validations.annotations.ValidUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTransactionDTO(
        @NotBlank
        @ValidUUID
        String accountFromId,

        @NotBlank
        @ValidUUID
        String accountToId,

        @NotNull
        BigDecimal amount
) {
}
