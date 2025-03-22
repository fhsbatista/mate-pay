package com.matepay.wallet_core.infra.jpa;

import com.matepay.wallet_core.data.models.AccountDb;
import com.matepay.wallet_core.data.models.ClientDb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaClientRepository extends JpaRepository<ClientDb, UUID> {
}
