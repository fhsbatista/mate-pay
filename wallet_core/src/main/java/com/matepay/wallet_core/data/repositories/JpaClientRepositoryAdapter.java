package com.matepay.wallet_core.data.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.data.models.AccountDb;
import com.matepay.wallet_core.data.models.ClientDb;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.repositories.AccountRepository;
import com.matepay.wallet_core.domain.repositories.ClientRepository;
import com.matepay.wallet_core.infra.jpa.JpaAccountRepository;
import com.matepay.wallet_core.infra.jpa.JpaClientRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaClientRepositoryAdapter implements ClientRepository {
    private final JpaClientRepository jpa;

    public JpaClientRepositoryAdapter(JpaClientRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Client get(UUID uuid) throws Exceptions.ClientNotFound {
        final Optional<ClientDb> client = jpa.findById(uuid);
        if (client.isEmpty()) throw new Exceptions.ClientNotFound();

        return client.get().toDomainWithNoAccounts();
    }

    @Override
    public Client save(Client client) {
        return jpa.save(ClientDb.fromDomainWithNoAccounts(client)).toDomainWithNoAccounts();
    }
}
