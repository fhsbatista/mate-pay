package com.matepay.wallet_core.domain.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Transaction;

import java.util.UUID;

public interface TransactionRepository {
    Transaction get(UUID uuid) throws Exceptions.TransactionNotFound;
    Transaction save(Transaction transaction) throws Exceptions;
}
