package com.matepay.wallet_core.domain.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Client;

import java.util.UUID;

public interface ClientRepository {
    Client get(UUID uuid) throws Exceptions.ClientNotFound;
    Client save(Client client);
}
