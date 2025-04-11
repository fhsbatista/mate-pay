package com.matepay.wallet_core.data.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.data.models.TransactionDb;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.entities.Transaction;
import com.matepay.wallet_core.infra.jpa.JpaTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JpaTransactionRepositoryAdapterTest {
    @Mock
    private JpaTransactionRepository jpaTransactionRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    JpaTransactionRepositoryAdapter makeSut() {
        return new JpaTransactionRepositoryAdapter(jpaTransactionRepository);
    }

    @Nested
    @DisplayName("get")
    class GetTests {
        @Test
        void shouldThrowIfTransactionIsNotFound() {
            final var sut = makeSut();
            final var uuid = UUID.randomUUID();
            when(jpaTransactionRepository.findById(uuid)).thenReturn(Optional.empty());

            assertThrows(Exceptions.TransactionNotFound.class, () -> sut.get(uuid));
        }
    }

    @Nested
    @DisplayName("save")
    class SaveTests {
        @Test
        void shouldSaveTransactionAndReturnDomainEntity() throws Exceptions {
            final var sut = makeSut();
            final var clientFrom = new Client("John Doe", "john.doe@mocks.com");
            final var clientTo = new Client("Mark Doe", "mark.doe@mocks.com");
            final var accountFrom = new Account(clientFrom);
            final var accountTo = new Account(clientTo);
            final var amount = BigDecimal.valueOf(100.0);
            final var transaction = new Transaction(accountFrom, accountTo, amount);
            when(jpaTransactionRepository.save(any())).thenReturn(TransactionDb.fromDomain(transaction));

            final var result = sut.save(transaction);

            verify(jpaTransactionRepository, times(1)).save(TransactionDb.fromDomain(transaction));
            assertEquals(transaction, result);

        }
    }
}