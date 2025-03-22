package com.matepay.wallet_core.domain.usecases;

import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateClientUsecaseTest {
    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    CreateClientUsecase makeSut() {
        return new CreateClientUsecase(clientRepository);
    }

    CreateClientUsecase.Input makeInput() {
        return new CreateClientUsecase.Input("John doe", "john.doe@johns.com");
    }

    Client makeClient(CreateClientUsecase.Input input) {
        return new Client(input.name(), input.email());
    }

    void mockSuccess(Client client) {
        when(clientRepository.save(any())).thenReturn(client);
    }

    @Test
    void shouldCallClientRepository() {
        final var usecase = makeSut();
        final var input = makeInput();
        final var client = makeClient(input);
        mockSuccess(client);

        usecase.execute(input);

        verify(clientRepository, times(1)).save(any());
    }

    @Test
    void shouldReturnCorrectClientOnRepositorySuccess() {
        final var usecase = makeSut();
        final var input = makeInput();
        final var client = makeClient(input);
        mockSuccess(client);

        final Client result = usecase.execute(input);

        assertEquals(client, result);
    }
}