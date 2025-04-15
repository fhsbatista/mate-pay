package com.matepay.balances.domain.usecases;

import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;
import com.matepay.balances.domain.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class RegisterBalanceUpdatedUsecase {
    public record Input(UUID accountId, BigDecimal balance) {

    }

    @Autowired
    private AccountRepository accountRepository;

    public Account execute(Input input) throws Exceptions.AccountNotFound {
        return accountRepository.registerBalanceUpdate(input.accountId, input.balance);
    }
}
