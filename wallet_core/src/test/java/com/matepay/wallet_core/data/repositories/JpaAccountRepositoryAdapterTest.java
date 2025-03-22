package com.matepay.wallet_core.data.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.data.models.AccountDb;
import com.matepay.wallet_core.data.models.ClientDb;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.infra.jpa.JpaAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    }

    @Nested
    @DisplayName("save")
    class SaveTests {
        @Test
        void shouldSaveAccountAndReturnDomainEntity() throws Exceptions {
            final var sut = makeSut();
            final var client = new Client("John Doe", "john.doe@mock.com");
            final var account = new Account(client);
            final var accountDb = new AccountDb(
                    account.getUuid(),
                    ClientDb.fromDomainWithNoAccounts(account.getClient()),
                    account.getBalance(),
                    account.getCreatedAt(),
                    account.getUpdatedAt()
            );

            when(jpaAccountRepository.save(any())).thenReturn(accountDb);

            final var result = sut.save(account);

            verify(jpaAccountRepository).save(any());
            assertEquals(accountDb.toDomain(), result);
        }
    }
}