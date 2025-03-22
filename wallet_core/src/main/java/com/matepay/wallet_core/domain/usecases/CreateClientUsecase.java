package com.matepay.wallet_core.domain.usecases;

import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateClientUsecase {
    public record Input(String name, String email) {
    }

    @Autowired
    private final ClientRepository clientRepository;

    public CreateClientUsecase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(Input input) {
        final Client client = clientRepository.save(new Client(input.name(), input.email()));
        return client;
    }


}
