package com.matepay.balances.presentation.controllers;

import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;
import com.matepay.balances.domain.usecases.GetAccountUsecase;
import com.matepay.balances.presentation.presenters.ErrorPresenter;
import com.matepay.balances.presentation.validations.annotations.ValidUUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("balances")
@Validated
public class AccountsController {
    @Autowired
    private GetAccountUsecase getAccountUsecase;

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable @ValidUUID String id) {
        try {
            final GetAccountUsecase.Input input = new GetAccountUsecase.Input(UUID.fromString(id));
            final Account account = getAccountUsecase.execute(input);
            return ResponseEntity.ok(account);
        } catch (Exceptions e) {
            return ResponseEntity.badRequest().body(new ErrorPresenter(e.toString()));
        }
    }
}
