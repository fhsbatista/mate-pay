package com.matepay.wallet_core.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Nested
    @DisplayName("Initialization")
    class InitializationTests {
        @Test
        void shouldReturnAccountOnValidArgs() {
            final Client client = new Client("John Doe", "john.doe@johns.com");
            final Account account = new Account(client);
            assertEquals(client, account.getClient());
            assertEquals(BigDecimal.ZERO, account.getBalance());
        }

        @Test
        void shouldThrowIllegalArgumentsExceptionOnInvalidArgs() {
            assertThrows(IllegalArgumentException.class, () -> new Account(null));
        }
    }

    @Nested
    @DisplayName("Operations")
    class OperationsTests {
        @Test
        void shouldUpdateBalanceCorrectlyOnCredit() {
            final Client client = new Client("John Doe", "john.doe@johns.com");
            final Account account = new Account(client);
            final Instant updatedAtBeforeCredit = account.getUpdatedAt();

            account.credit(BigDecimal.valueOf(1.0));

            assertEquals(BigDecimal.valueOf(1.0), account.getBalance());
            assertNotEquals(updatedAtBeforeCredit, account.getUpdatedAt());
        }

        @Test
        void shouldUpdateBalanceCorrectlyOnDebit() {
            final Client client = new Client("John Doe", "john.doe@johns.com");
            final Account account = new Account(client);
            final Instant updatedAtBeforeCredit = account.getUpdatedAt();
            account.credit(BigDecimal.valueOf(10.0));

            account.debit(BigDecimal.valueOf(1.0));

            assertEquals(BigDecimal.valueOf(9.0), account.getBalance());
            assertNotEquals(updatedAtBeforeCredit, account.getUpdatedAt());
        }

    }



}