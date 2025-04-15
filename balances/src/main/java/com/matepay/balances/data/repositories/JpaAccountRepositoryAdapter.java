package com.matepay.balances.data.repositories;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matepay.balances.data.models.AccountDb;
import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;
import com.matepay.balances.domain.repositories.AccountRepository;
import com.matepay.balances.infra.jpa.JpaAccountRepository;

@Repository
public class JpaAccountRepositoryAdapter implements AccountRepository {
    @Autowired
    private final JpaAccountRepository jpa;

    public JpaAccountRepositoryAdapter(JpaAccountRepository jpa) {
        this.jpa = jpa;
    }


    @Override
    public Account get(UUID uuid) throws Exceptions.AccountNotFound {
        final Optional<AccountDb> account = jpa.findById(uuid);
        if (account.isEmpty()) throw new Exceptions.AccountNotFound();

        return account.get().toDomain();
    }

    @Override
    public Account registerBalanceUpdate(UUID accountId, BigDecimal updatedBalance) throws Exceptions.AccountNotFound {
        final Optional<AccountDb> accountDb = jpa.findById(accountId);
        if (accountDb.isEmpty()) throw new Exceptions.AccountNotFound();

        accountDb.get().updateBalance(updatedBalance);
        jpa.save(accountDb.get());

        return accountDb.get().toDomain();
    }

    @Override
    public Account create(Account account) {
        final var accountDb = AccountDb.fromDomain(account);
        final var savedAccount = jpa.save(accountDb);
        return savedAccount.toDomain();
    }
}
