package com.matepay.balances.domain.usecases;

import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;
import com.matepay.balances.domain.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class GetAccountUsecase {
    public record Input(UUID accountId) {
    }

    @Autowired
    private AccountRepository accountRepository;

    public Account execute(Input input) throws Exceptions.AccountNotFound {
        return accountRepository.get(input.accountId);
    }
}
