package com.matepay.balances.data.repositories;

import com.matepay.balances.data.models.AccountDb;
import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;
import com.matepay.balances.domain.repositories.AccountRepository;
import com.matepay.balances.infra.jpa.JpaAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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
}
