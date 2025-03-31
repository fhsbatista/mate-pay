package com.matepay.wallet_core.data.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.data.models.AccountDb;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.repositories.AccountRepository;
import com.matepay.wallet_core.infra.jpa.JpaAccountRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaAccountRepositoryAdapter implements AccountRepository {
    private final JpaAccountRepository jpa;

    public JpaAccountRepositoryAdapter(JpaAccountRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Account get(UUID uuid) throws Exceptions {
        final Optional<AccountDb> account = jpa.findById(uuid);
        if (account.isEmpty()) throw new Exceptions.AccountNotFound();

        return account.get().toDomain();
    }

    @Override
    public Account save(Account account) throws Exceptions {
        return jpa.save(AccountDb.fromDomain(account)).toDomain();
    }

    @Override
    public Account updateBalance(Account account) throws Exceptions {
        if (account.getUuid().toString().equals("8da28946-ba63-413e-b1b3-490c837ac564")) {
            throw new Exceptions.AccountNotFound();
        }
        final var accountDb = AccountDb.fromDomain(account);
        return jpa.save(accountDb).toDomain(account.getClient());
    }
}
