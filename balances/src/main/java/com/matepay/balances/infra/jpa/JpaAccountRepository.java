package com.matepay.balances.infra.jpa;

import com.matepay.balances.data.models.AccountDb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaAccountRepository extends JpaRepository<AccountDb, UUID> {
}
