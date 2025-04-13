package com.matepay.balances.domain.usecases;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;
import com.matepay.balances.domain.repositories.AccountRepository;

class GetAccountUsecaseTest {
    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    GetAccountUsecase makeSut() {
        return new GetAccountUsecase(accountRepository);
    }

    GetAccountUsecase.Input makeInput(UUID uuid) {
        return new GetAccountUsecase.Input(uuid);
    }

    Account makeAccount(UUID uuid) {
        return new Account(
                uuid,
                new BigDecimal("100.00"),
                Instant.now()
        );
    }

    void mockSuccessGetAccount(Account account) throws Exceptions.AccountNotFound {
        when(accountRepository.get(account.getUuid())).thenReturn(account);
    }

    void mockFailureGetAccount(UUID uuid) throws Exceptions.AccountNotFound {
        when(accountRepository.get(uuid)).thenThrow(new Exceptions.AccountNotFound());
    }

    @Test
    void shouldCallAccountRepositoryWithCorrectId() throws Exceptions.AccountNotFound {
        final var sut = makeSut();
        final var uuid = UUID.randomUUID();
        final var input = makeInput(uuid);
        final var account = makeAccount(uuid);
        mockSuccessGetAccount(account);

        sut.execute(input);

        verify(accountRepository).get(uuid);
    }

    @Test
    void shouldThrowIfAccountCannotBeFound() throws Exceptions.AccountNotFound {
        final var sut = makeSut();
        final var uuid = UUID.randomUUID();
        final var input = makeInput(uuid);
        mockFailureGetAccount(uuid);

        assertThrows(Exceptions.AccountNotFound.class, () -> sut.execute(input));
    }

    @Test
    void shouldReturnCorrectAccountOnRepositorySuccess() throws Exceptions.AccountNotFound {
        final var sut = makeSut();
        final var uuid = UUID.randomUUID();
        final var input = makeInput(uuid);
        final var account = makeAccount(uuid);
        mockSuccessGetAccount(account);

        final Account result = sut.execute(input);

        assertEquals(account, result);
    }
} 