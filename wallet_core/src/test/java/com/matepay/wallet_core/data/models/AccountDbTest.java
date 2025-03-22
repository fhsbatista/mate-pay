package com.matepay.wallet_core.data.models;

import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountDbTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        void shouldBeAbleToConvertToDomainEntity() {
            final var clientDb = new ClientDb(
                    UUID.randomUUID(),
                    "John Doe",
                    "john.doe@mock.com",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );
            final var accountDb = new AccountDb(
                    UUID.randomUUID(),
                    clientDb,
                    BigDecimal.ZERO,
                    Instant.now(),
                    Instant.now()
            );
            clientDb.setAccounts(List.of(accountDb));

            final var result = accountDb.toDomain();

            assertEquals(accountDb.getId(), result.getUuid());
            assertEquals(accountDb.getBalance(), result.getBalance());
            assertEquals(accountDb.getCreatedAt(), result.getCreatedAt());
            assertEquals(accountDb.getUpdatedAt(), result.getUpdatedAt());
            assertEquals(
                    accountDb.getClient().toDomainWithNoAccounts(),
                    result.getClient()
            );
        }
    }

    @Nested
    @DisplayName("fromDomain")
    class FromDomainTests {
        @Test
        void shouldBeAbleToConvertFromDomainEntity() {
            final var client = new Client("John Doe", "john.doe@mock.com");
            final var account = new Account(client);

            final var result = AccountDb.fromDomain(account);

            assertEquals(account.getUuid(), result.getId());
            assertEquals(ClientDb.fromDomainWithNoAccounts(client), result.getClient());
            assertEquals(account.getClient().getName(), result.getClient().getName());
            assertEquals(account.getClient().getEmail(), result.getClient().getEmail());
            assertEquals(account.getBalance(), result.getBalance());
            assertEquals(account.getCreatedAt(), result.getCreatedAt());
            assertEquals(account.getUpdatedAt(), result.getUpdatedAt());
        }
    }
}