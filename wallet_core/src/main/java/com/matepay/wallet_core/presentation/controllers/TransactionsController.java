package com.matepay.wallet_core.presentation.controllers;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Transaction;
import com.matepay.wallet_core.domain.usecases.CreateTransactionUsecase;
import com.matepay.wallet_core.presentation.controllers.dtos.CreateTransactionDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
    @Autowired
    private CreateTransactionUsecase createTransactionUsecase;

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody @Valid CreateTransactionDTO data) throws Exceptions {
        final CreateTransactionUsecase.Input input = new CreateTransactionUsecase.Input(
                UUID.fromString(data.accountFromId()),
                UUID.fromString(data.accountFromTo()),
                data.amount()
        );
        final Transaction transaction = createTransactionUsecase.execute(input);
        return ResponseEntity.ok(transaction);
    }
}
