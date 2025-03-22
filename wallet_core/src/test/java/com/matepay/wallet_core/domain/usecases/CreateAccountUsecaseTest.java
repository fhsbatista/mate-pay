package com.matepay.wallet_core.domain.usecases;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.repositories.AccountRepository;
import com.matepay.wallet_core.domain.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAccountUsecaseTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    CreateAccountUsecase makeSut() {
        return new CreateAccountUsecase(accountRepository, clientRepository);
    }

    Client makeClient() {
        return new Client("John Doe", "john.doe@mocks.com");
    }

    CreateAccountUsecase.Input makeInput(UUID uuid) {
        return new CreateAccountUsecase.Input(uuid);
    }

    Account makeAccount(Client client) {
        return new Account(client);
    }

    void mockSuccessGetClient(Client client) throws Exceptions.ClientNotFound {
        when(clientRepository.get(client.getUuid())).thenReturn(client);
    }

    void mockFailureGetClient(Client client) throws Exceptions.ClientNotFound {
        when(clientRepository.get(client.getUuid())).thenThrow(new Exceptions.ClientNotFound());
    }

    void mockSuccessSaveAccount(Account account) {
        when(accountRepository.save(any())).thenReturn(account);
    }

    @Test
    void shouldCallClientRepositoryWithCorrectId() throws Exceptions {
        final var usecase = makeSut();
        final var client = makeClient();
        final var input = makeInput(client.getUuid());
        final var account = makeAccount(client);
        mockSuccessGetClient(client);
        mockSuccessSaveAccount(account);

        usecase.execute(input);

        verify(clientRepository, times(1)).get(client.getUuid());
    }

    @Test
    void shouldThrowIfClientCannotBeFound() throws Exceptions.ClientNotFound {
        final var usecase = makeSut();
        final var client = makeClient();
        final var input = makeInput(client.getUuid());
        mockFailureGetClient(client);

        assertThrows(Exceptions.ClientNotFound.class, () -> usecase.execute(input));
    }

    @Test
    void shouldCallAccountRepository() throws Exceptions {
        final var usecase = makeSut();
        final var client = makeClient();
        final var input = makeInput(client.getUuid());
        final var account = makeAccount(client);
        mockSuccessGetClient(client);
        mockSuccessSaveAccount(account);

        usecase.execute(input);

        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void shouldReturnCorrectAccountOnRepositorySuccess() throws Exceptions.ClientNotFound {
        final var usecase = makeSut();
        final var client = makeClient();
        final var input = makeInput(client.getUuid());
        final var account = makeAccount(client);
        mockSuccessGetClient(client);
        mockSuccessSaveAccount(account);

        final CreateAccountUsecase.Output result = usecase.execute(input);

        assertEquals(account, result.account());
    }


}