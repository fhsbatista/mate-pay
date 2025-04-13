package com.matepay.balances.data.repositories;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.matepay.balances.data.models.AccountDb;
import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.infra.jpa.JpaAccountRepository;

class JpaAccountRepositoryAdapterTest {
    @Mock
    private JpaAccountRepository jpaAccountRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    JpaAccountRepositoryAdapter makeSut() {
        return new JpaAccountRepositoryAdapter(jpaAccountRepository);
    }

    @Nested
    @DisplayName("get")
    class GetTests {
        @Test
        void shouldThrowIfAccountIsNotFound() {
            final var sut = makeSut();
            final var uuid = UUID.randomUUID();
            when(jpaAccountRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(Exceptions.AccountNotFound.class, () -> sut.get(uuid));
        }

        @Test
        void shouldReturnAccountWhenFound() throws Exceptions.AccountNotFound {
            final var sut = makeSut();
            final var uuid = UUID.randomUUID();
            final var balance = new BigDecimal("100.00");
            final var updatedAt = Instant.now();
            
            final var accountDb = new AccountDb(uuid, balance, updatedAt);
            when(jpaAccountRepository.findById(uuid)).thenReturn(Optional.of(accountDb));

            final var result = sut.get(uuid);

            assertEquals(uuid, result.getUuid());
            assertEquals(balance, result.getBalance());
            assertEquals(updatedAt, result.getUpdatedAt());
        }
    }
} 