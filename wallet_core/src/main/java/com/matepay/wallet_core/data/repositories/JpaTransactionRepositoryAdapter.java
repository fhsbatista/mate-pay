package com.matepay.wallet_core.data.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.data.models.TransactionDb;
import com.matepay.wallet_core.domain.entities.Transaction;
import com.matepay.wallet_core.domain.repositories.TransactionRepository;
import com.matepay.wallet_core.infra.jpa.JpaTransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaTransactionRepositoryAdapter implements TransactionRepository {
    private final JpaTransactionRepository jpa;

    public JpaTransactionRepositoryAdapter(JpaTransactionRepository jpa) {
        this.jpa = jpa;
    }
    @Override
    public Transaction get(UUID uuid) throws Exceptions.TransactiontNotFound {
        final var transaction = jpa.findById(uuid);
        if (transaction.isEmpty()) throw new Exceptions.TransactiontNotFound();

        return null;
    }

    @Override
    public Transaction save(Transaction transaction) throws Exceptions {
        return jpa.save(TransactionDb.fromDomain(transaction)).toDomain();
    }
}
