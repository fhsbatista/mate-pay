package com.matepay.wallet_core.domain.usecases;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.repositories.AccountRepository;
import com.matepay.wallet_core.domain.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class CreateAccountUsecase {
    public record Input(UUID clientId) {
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    public CreateAccountUsecase(
            AccountRepository accountRepository,
            ClientRepository clientRepository
    ) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public Account execute(Input input) throws Exceptions.ClientNotFound {
        final Client client = clientRepository.get(input.clientId);
        final Account account = new Account(client);
        return accountRepository.save(account);
    }
}
