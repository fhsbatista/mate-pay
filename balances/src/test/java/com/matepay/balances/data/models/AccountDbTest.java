package com.matepay.balances.data.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.matepay.balances.domain.entities.Account;

class AccountDbTest {

    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        void shouldBeAbleToConvertToDomainEntity() {
            final var id = UUID.randomUUID();
            final var balance = new BigDecimal("300.00");
            final var updatedAt = Instant.now();
            final var accountDb = new AccountDb(id, balance, updatedAt);

            final var result = accountDb.toDomain();

            assertNotNull(result);
            assertEquals(id, result.getUuid());
            assertEquals(balance, result.getBalance());
            assertEquals(updatedAt, result.getUpdatedAt());
        }

        @Test
        void shouldBeAbleToConvertEmptyAccountDbToDomainEntity() {
            final var accountDb = new AccountDb();

            final var result = accountDb.toDomain();

            assertNotNull(result);
            assertNull(result.getUuid());
            assertNull(result.getBalance());
            assertNull(result.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("fromDomain")
    class FromDomainTests {
        @Test
        void shouldBeAbleToConvertFromDomainEntity() {
            final var id = UUID.randomUUID();
            final var balance = new BigDecimal("200.00");
            final var updatedAt = Instant.now();
            final var domainAccount = new Account(id, balance, updatedAt);

            final var result = AccountDb.fromDomain(domainAccount);

            assertNotNull(result);
            assertEquals(id, result.getId());
            assertEquals(balance, result.getBalance());
            assertEquals(updatedAt, result.getUpdatedAt());
        }
    }

} 