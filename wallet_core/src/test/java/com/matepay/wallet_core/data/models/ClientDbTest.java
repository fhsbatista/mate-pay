package com.matepay.wallet_core.data.models;

import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientDbTest {
    AccountDb makeAccountDb(ClientDb client) {
        return AccountDb.fromDomain(new Account(client.toDomainWithNoAccounts()));
    }

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
            final var account1 = makeAccountDb(clientDb);
            final var account2 = makeAccountDb(clientDb);
            clientDb.setAccounts(List.of(account1, account2));

            final var result = clientDb.toDomainWithNoAccounts();

            assertEquals(clientDb.getId(), result.getUuid());
            assertEquals(clientDb.getName(), result.getName());
            assertEquals(clientDb.getEmail(), result.getEmail());
            assertEquals(clientDb.getCreatedAt(), result.getCreatedAt());
            assertEquals(clientDb.getUpdatedAt(), result.getUpdatedAt());
            assertEquals(List.of(), result.getAccounts());
        }
    }

    @Nested
    @DisplayName("fromDomain")
    class FromDomainTests {
        @Test
        void shouldBeAbleToConvertFromDomainEntity() {
            final var client = new Client("John Doe", "john.doe@mock.com");
            final var result = ClientDb.fromDomainWithNoAccounts(client);

            assertEquals(client.getUuid(), result.getId());
            assertEquals(client.getName(), result.getName());
            assertEquals(client.getEmail(), result.getEmail());
            assertEquals(List.of(), result.getAccounts());
            assertEquals(client.getCreatedAt(), result.getCreatedAt());
            assertEquals(client.getUpdatedAt(), result.getUpdatedAt());
        }
    }
}