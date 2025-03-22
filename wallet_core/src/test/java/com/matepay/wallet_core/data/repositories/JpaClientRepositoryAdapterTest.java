package com.matepay.wallet_core.data.repositories;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.data.models.AccountDb;
import com.matepay.wallet_core.data.models.ClientDb;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.infra.jpa.JpaAccountRepository;
import com.matepay.wallet_core.infra.jpa.JpaClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JpaClientRepositoryAdapterTest {
    @Mock
    private JpaClientRepository jpaClientRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    JpaClientRepositoryAdapter makeSut() {
        return new JpaClientRepositoryAdapter(jpaClientRepository);
    }

    @Nested
    @DisplayName("get")
    class GetTests {
        @Test
        void shouldThrowIfClientIsNotFound() {
            final var sut = makeSut();
            final var uuid = UUID.randomUUID();
            when(jpaClientRepository.findById(any())).thenReturn(Optional.empty());

            assertThrows(Exceptions.ClientNotFound.class, () -> sut.get(uuid));
        }
    }

    @Nested
    @DisplayName("save")
    class SaveTests {
        @Test
        void shouldSaveClientAndReturnDomainEntity() throws Exceptions {
            final var sut = makeSut();
            final var client = new Client("John Doe", "john.doe@mock.com");
            final var clientDb = ClientDb.fromDomainWithNoAccounts(client);

            when(jpaClientRepository.save(any())).thenReturn(clientDb);

            final var result = sut.save(client);

            verify(jpaClientRepository).save(any());
            assertEquals(clientDb.toDomainWithNoAccounts(), result);
        }
    }
}