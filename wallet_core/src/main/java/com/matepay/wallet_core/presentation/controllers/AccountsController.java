package com.matepay.wallet_core.presentation.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.usecases.CreateAccountUsecase;
import com.matepay.wallet_core.domain.usecases.GetAccountUsecase;
import com.matepay.wallet_core.presentation.controllers.dtos.CreateAccountDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    @Autowired
    private CreateAccountUsecase createAccountUsecase;

    @Autowired
    private GetAccountUsecase getAccountUsecase;

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody @Valid CreateAccountDTO data) throws Exceptions {
        final var input = new CreateAccountUsecase.Input(UUID.fromString(data.clientId()));
        final Account createdAccount = createAccountUsecase.execute(input);
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> get(@PathVariable String id) throws Exceptions {
        final var input = new GetAccountUsecase.Input(UUID.fromString(id));
        final Account account = getAccountUsecase.execute(input);
        return ResponseEntity.ok(account);
    }
}
