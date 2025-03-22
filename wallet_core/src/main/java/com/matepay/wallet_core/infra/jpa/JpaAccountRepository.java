package com.matepay.wallet_core.infra.jpa;

import com.matepay.wallet_core.data.models.AccountDb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAccountRepository extends JpaRepository<AccountDb, UUID> {
}
