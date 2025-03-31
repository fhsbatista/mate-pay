package com.matepay.wallet_core.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Transaction;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.usecases.CreateTransactionUsecase;
import com.matepay.wallet_core.presentation.controllers.dtos.CreateTransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionsController.class)
class TransactionsControllerTest {
    @MockitoBean
    private CreateTransactionUsecase createTransactionUsecase;

    @Autowired
    @InjectMocks
    private TransactionsController sut;

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

    Transaction makeTransaction() throws Exceptions {
        final var clientFrom = new Client("from", "mock");
        final var clientTo = new Client("to", "mock");
        final var accountFrom = new Account(clientFrom);
        final var accountTo = new Account(clientTo);
        return new Transaction(accountFrom, accountTo, BigDecimal.valueOf(200.0));
    }

    Transaction mockSuccess() throws Exceptions {
        final var transaction = makeTransaction();
        when(createTransactionUsecase.execute(any())).thenReturn(transaction);
        return transaction;
    }

    @Nested
    @DisplayName("Create")
    class CreateTests {
        @Test
        void shouldReturnErrorOnBlankAccountFromId() throws Exception {
            final var transaction = makeTransaction();
            final var input = new CreateTransactionDTO(
                    "",
                    transaction.getTo().getUuid().toString(),
                    transaction.getAmount()
            );
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[*].field").value(hasItem("accountFromId")))
                    .andExpect(jsonPath("$[*].message").value(hasItem("must not be blank")));
        }

        @Test
        void shouldReturnErrorOnBlankAccountFromTo() throws Exception {
            final var transaction = makeTransaction();
            final var input = new CreateTransactionDTO(
                    transaction.getTo().getUuid().toString(),
                    "",
                    transaction.getAmount()
            );
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[*].field").value(hasItem("accountFromTo")))
                    .andExpect(jsonPath("$[*].message").value(hasItem("must not be blank")));
        }


        @Test
        void shouldReturnErrorOnAccountFromWithInvalidUUID() throws Exception {
            final var transaction = makeTransaction();
            final var input = new CreateTransactionDTO(
                    "invalid-uuid",
                    transaction.getTo().getUuid().toString(),
                    transaction.getAmount()
            );
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[*].field").value(hasItem("accountFromId")))
                    .andExpect(jsonPath("$[*].message").value(hasItem("invalid UUID")));
        }

        @Test
        void shouldReturnErrorOnAccountToWithInvalidUUID() throws Exception {
            final var transaction = makeTransaction();
            final var input = new CreateTransactionDTO(
                    transaction.getFrom().getUuid().toString(),
                    "invalid-uuid",
                    transaction.getAmount()
            );
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[*].field").value(hasItem("accountFromTo")))
                    .andExpect(jsonPath("$[*].message").value(hasItem("invalid UUID")));
        }

        @Test
        void shouldReturnErrorOnNullAmount() throws Exception {
            final var transaction = makeTransaction();
            final var input = new CreateTransactionDTO(
                    transaction.getFrom().getUuid().toString(),
                    transaction.getTo().getUuid().toString(),
                    null
            );
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/transactions")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[*].field").value(hasItem("amount")))
                    .andExpect(jsonPath("$[*].message").value(hasItem("must not be null")));
        }

        @Test
        void shouldReturnCorrectDataOnUsecaseSuccess() throws Exception {
            final var transaction = mockSuccess();
            final var input = new CreateTransactionDTO(
                    transaction.getFrom().getUuid().toString(),
                    transaction.getTo().getUuid().toString(),
                    BigDecimal.valueOf(200.0)
            );
            final var json = new ObjectMapper().writeValueAsString(input);

            var result = mockMvc.perform(post("/api/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));


            result.andExpect(status().is2xxSuccessful())
                    .andDo(print())
                    .andExpect(jsonPath("$.uuid").value(transaction.getUuid().toString()))
                    .andExpect(jsonPath("$.from.uuid").value(transaction.getFrom().getUuid().toString()))
                    .andExpect(jsonPath("$.to.uuid").value(transaction.getTo().getUuid().toString()))
                    .andExpect(jsonPath("$.amount").value(transaction.getAmount().toString()));
        }
    }
}