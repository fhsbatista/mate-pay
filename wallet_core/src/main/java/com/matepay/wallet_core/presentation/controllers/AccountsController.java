package com.matepay.wallet_core.presentation.controllers;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.usecases.CreateAccountUsecase;
import com.matepay.wallet_core.presentation.controllers.dtos.CreateAccountDTO;
import com.matepay.wallet_core.presentation.controllers.dtos.CreateClientDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    @Autowired
    private CreateAccountUsecase createAccountUsecase;

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody @Valid CreateAccountDTO data) throws Exceptions {
        final var input = new CreateAccountUsecase.Input(UUID.fromString(data.clientId()));
        final Account createdAccount = createAccountUsecase.execute(input);
        return ResponseEntity.ok(createdAccount);
    }
}
