package com.matepay.wallet_core.data.models;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.entities.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDbTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        void shouldBeAbleToConvertToDomainEntity() throws Exceptions {
            final var clientTo = new Client("John Doe", "john.doe@mock.com");
            final var clientFrom = new Client("Mark Doe", "Mark.doe@mock.com");
            final var accountTo = new Account(clientTo);
            final var accountFrom = new Account(clientFrom);
            final var amount = BigDecimal.valueOf(100.0);
            final var transactionDb = new TransactionDb(
                    UUID.randomUUID(),
                    AccountDb.fromDomain(accountFrom),
                    AccountDb.fromDomain(accountTo),
                    amount,
                    Instant.now()
            );

            final var result = transactionDb.toDomain();

            assertEquals(transactionDb.getUuid(), result.getUuid());
            assertEquals(transactionDb.getFrom().toDomain(), result.getFrom());
            assertEquals(transactionDb.getTo().toDomain(), result.getTo());
            assertEquals(transactionDb.getAmount(), result.getAmount());
            assertEquals(transactionDb.getCreatedAt(), result.getCreatedAt());
        }
    }

    @Nested
    @DisplayName("toDomain")
    class FromDomainTests {
        @Test
        void shouldBeAbleToConvertFromDomainEntity() throws Exceptions {
            final var clientTo = new Client("John Doe", "john.doe@mock.com");
            final var clientFrom = new Client("Mark Doe", "Mark.doe@mock.com");
            final var accountTo = new Account(clientTo);
            final var accountFrom = new Account(clientFrom);
            final var amount = BigDecimal.valueOf(100.0);
            final var transaction = new Transaction(accountFrom, accountTo, amount);

            final var result = TransactionDb.fromDomain(transaction);

            assertEquals(transaction.getUuid(), result.getUuid());
            assertEquals(transaction.getFrom(), result.getFrom().toDomain());
            assertEquals(transaction.getTo(), result.getTo().toDomain());
            assertEquals(transaction.getAmount(), result.getAmount());
            assertEquals(transaction.getCreatedAt(), result.getCreatedAt());
        }
    }
}