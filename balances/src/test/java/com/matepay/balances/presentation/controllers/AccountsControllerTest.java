package com.matepay.balances.presentation.controllers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.mockito.InjectMocks;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.entities.Account;
import com.matepay.balances.domain.usecases.GetAccountUsecase;

@WebMvcTest(AccountsController.class)
class AccountsControllerTest {
    @MockitoBean
    private GetAccountUsecase getAccountUsecase;

    @Autowired
    @InjectMocks
    private AccountsController sut;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exceptions {
        MockitoAnnotations.openMocks(this);
        mockSuccess();
        mockMvc = MockMvcBuilders
                .standaloneSetup(sut)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    Account makeAccount() {
        return new Account(
                UUID.randomUUID(),
                BigDecimal.valueOf(100.0),
                Instant.now()
        );
    }

    void mockSuccess() throws Exceptions {
        final var account = makeAccount();
        when(getAccountUsecase.execute(any())).thenReturn(account);
    }

    @Nested
    @DisplayName("Get")
    class GetTests {
        @Test
        void shouldReturnErrorOnInvalidUUID() throws Exception {
            mockMvc.perform(get("/api/accounts/invalid-uuid")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("invalid UUID"));
        }

        @Test
        void shouldReturnErrorWhenAccountNotFound() throws Exception {
            final var uuid = UUID.randomUUID();
            when(getAccountUsecase.execute(any())).thenThrow(new Exceptions.AccountNotFound());

            mockMvc.perform(get("/api/accounts/" + uuid)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("AccountNotFound"));
        }

        @Test
        void shouldCallUsecaseWithCorrectId() throws Exception {
            final var uuid = UUID.randomUUID();
            final var account = makeAccount();
            when(getAccountUsecase.execute(any())).thenReturn(account);

            mockMvc.perform(get("/api/accounts/" + uuid)
                    .contentType(MediaType.APPLICATION_JSON));

            verify(getAccountUsecase, times(1)).execute(eq(new GetAccountUsecase.Input(uuid)));
        }

        @Test
        void shouldReturnAccountOnSuccess() throws Exception {
            final var uuid = UUID.randomUUID();
            final var account = makeAccount();
            when(getAccountUsecase.execute(any())).thenReturn(account);

            mockMvc.perform(get("/api/accounts/" + uuid)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.uuid").value(account.getUuid().toString()))
                    .andExpect(jsonPath("$.balance").value(account.getBalance().toString()));
        }
    }
} 