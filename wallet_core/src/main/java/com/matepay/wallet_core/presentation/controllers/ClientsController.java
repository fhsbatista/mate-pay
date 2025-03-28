package com.matepay.wallet_core.presentation.controllers;

import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.usecases.CreateClientUsecase;
import com.matepay.wallet_core.presentation.controllers.dtos.CreateClientDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientsController {
    @Autowired
    private CreateClientUsecase createClientUsecase;

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody @Valid CreateClientDTO data) {
        final var input = new CreateClientUsecase.Input(data.name(), data.email());
        Client createdClient = createClientUsecase.execute(input);

        return ResponseEntity.ok(createdClient);
    }
}
