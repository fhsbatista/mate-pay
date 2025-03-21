package com.matepay.wallet_core.entities;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    @Nested
    @DisplayName("Initialization")
    class InitializationTests {
        @Test
        void shouldReturnClientOnValidArgs() {
            final Client client = new Client("John Doe", "john.doe@johns.com");
            assertEquals("John Doe", client.getName());
            assertEquals("john.doe@johns.com", client.getEmail());
            assertEquals(List.of(), client.getAccounts());
        }

        @Test
        void shouldThrowIllegalArgumentsExceptionOnInvalidArgs() {
            assertThrows(IllegalArgumentException.class, () -> new Client("", "john.doe@johns.com"));
            assertThrows(IllegalArgumentException.class, () -> new Client("John Doe", ""));
        }
    }

    @Nested
    @DisplayName("Operations")
    class OperationsTests {
        @Test
        void shouldAddAccountOnAccountsListWhenAccountIsValid() throws Exceptions.AccountAlreadyBelongsToOtherClient {
            final Client client = new Client("John Doe", "john.doe@johns.com");
            final Account account = new Account(client);

            client.addAccount(account);

            assertTrue(client.getAccounts().contains(account));
        }

        @Test
        void shouldThrowWhenAccountAlreadyBelongsToOtherClient() {
            final Client client = new Client("John Doe", "john.doe@johns.com");
            final Client accountClient = new Client("Mark Doe", "mark.doe@johns.com");
            final Account account = new Account(accountClient);

            assertThrows(Exceptions.AccountAlreadyBelongsToOtherClient.class, () -> client.addAccount(account));
        }
    }


}