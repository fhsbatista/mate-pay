package com.matepay.wallet_core.entities;

import com.matepay.wallet_core.Exceptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Nested
    @DisplayName("Initialization")
    class InitializationTests {
        @Test
        void shouldReturnTransactionOnValidArgs() throws Exceptions {
            final Client clientFrom = new Client("John Doe", "john.doe@johns.com");
            final Account accountFrom = new Account(clientFrom);
            accountFrom.credit(BigDecimal.valueOf(100));
            final Client clientTo = new Client("Mark Doe", "mark.doe@johns.com");
            final Account accountTo = new Account(clientTo);

            final Transaction transaction = new Transaction(accountFrom, accountTo, BigDecimal.valueOf(50.0));

            assertEquals(accountFrom, transaction.getFrom());
            assertEquals(accountTo, transaction.getTo());
            assertEquals(BigDecimal.valueOf(50.0), transaction.getAmount());
        }

        @Test
        void shouldThrowIfArgsOnInvalidArgs() {
            final Client clientFrom = new Client("John Doe", "john.doe@johns.com");
            final Account accountFrom = new Account(clientFrom);
            accountFrom.credit(BigDecimal.valueOf(100));
            final Client clientTo = new Client("Mark Doe", "mark.doe@johns.com");
            final Account accountTo = new Account(clientTo);

            assertThrows(
                    Exceptions.AmountMustBePresent.class,
                    () -> new Transaction(accountFrom, accountTo, BigDecimal.valueOf(0.0))
            );
            assertThrows(
                    Exceptions.AmountMustBePresent.class,
                    () -> new Transaction(accountFrom, accountTo, null)
            );
            assertThrows(
                    Exceptions.AccountFromMustBePresent.class,
                    () -> new Transaction(null, accountTo, BigDecimal.valueOf(0.0))
            );
            assertThrows(
                    Exceptions.AccountToMustBePresent.class,
                    () -> new Transaction(accountFrom, null, BigDecimal.valueOf(0.0))
            );
        }

        @Test
        void shouldThrowIfAccountFromHasNotEnoughBalance() {
            final Client clientFrom = new Client("John Doe", "john.doe@johns.com");
            final Account accountFrom = new Account(clientFrom);
            accountFrom.credit(BigDecimal.valueOf(100));
            final Client clientTo = new Client("Mark Doe", "mark.doe@johns.com");
            final Account accountTo = new Account(clientTo);

            assertThrows(
                    Exceptions.NotEnoughBalance.class,
                    () -> new Transaction(accountFrom, accountTo, BigDecimal.valueOf(101.0))
            );
        }
    }

    @Nested
    @DisplayName("operations")
    class OperationsTests {
        @Test
        void shouldUpdateBalancesOnCommit() throws Exceptions {
            final Client clientFrom = new Client("John Doe", "john.doe@johns.com");
            final Account accountFrom = new Account(clientFrom);
            accountFrom.credit(BigDecimal.valueOf(100));
            final Client clientTo = new Client("Mark Doe", "mark.doe@johns.com");
            final Account accountTo = new Account(clientTo);

            final Transaction transaction = new Transaction(accountFrom, accountTo, BigDecimal.valueOf(50.0));
            transaction.commit();

            assertEquals(BigDecimal.valueOf(50.0), accountFrom.getBalance());
            assertEquals(BigDecimal.valueOf(50.0), accountTo.getBalance());
        }
    }

}