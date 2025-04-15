package com.matepay.wallet_core.domain.usecases;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.repositories.AccountRepository;
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

    public Account execute(Input input) throws Exceptions {
        return accountRepository.get(input.accountId);
    }
} 