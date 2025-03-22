package com.matepay.wallet_core.domain.repositories;

import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Transaction;

import java.util.UUID;

public interface TransactionRepository {
    Transaction get(UUID uuid);
    Transaction save(Transaction transaction);
}
