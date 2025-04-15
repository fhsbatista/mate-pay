package com.matepay.balances.domain.usecases;

import com.matepay.balances.domain.entities.Account;
import com.matepay.balances.domain.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class CreateAccountUsecase {
    public record Input(UUID accountId, BigDecimal initialBalance) {
    }

    @Autowired
    private AccountRepository accountRepository;

    public Account execute(Input input) {
        final var account = new Account(
                input.accountId,
                input.initialBalance,
                Instant.now()
        );
        return accountRepository.create(account);
    }
} 