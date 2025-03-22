package com.matepay.wallet_core.infra.jpa;

import com.matepay.wallet_core.data.models.TransactionDb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaTransactionRepository extends JpaRepository<TransactionDb, UUID> {
}
