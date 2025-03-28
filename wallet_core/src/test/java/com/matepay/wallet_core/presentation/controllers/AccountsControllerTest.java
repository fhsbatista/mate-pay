package com.matepay.wallet_core.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.usecases.CreateAccountUsecase;
import com.matepay.wallet_core.domain.usecases.CreateClientUsecase;
import com.matepay.wallet_core.presentation.controllers.dtos.CreateAccountDTO;
import com.matepay.wallet_core.presentation.controllers.dtos.CreateClientDTO;
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

import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountsController.class)
class AccountsControllerTest {
    @MockitoBean
    private CreateAccountUsecase createAccountUsecase;

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

    void mockSuccess() throws Exceptions {
        final var client = new Client("mock", "mock");
        final var account = new Account(client);
        when(createAccountUsecase.execute(any())).thenReturn(account);
    }

    @Nested
    @DisplayName("Create")
    class CreateTests {
        @Test
        void shouldReturnErrorOnBlankClientId() throws Exception {
            final var input = new CreateAccountDTO("");
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[*].field").value(hasItem("clientId")))
                    .andExpect(jsonPath("$[*].message").value(hasItem("must not be empty")));
        }

        @Test
        void shouldReturnErrorOnInvalidUUID() throws Exception {
            final var input = new CreateAccountDTO("abcdefgh-ijkl-mnop-qrst-uvwxyz123456");
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/accounts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("clientId"))
                    .andExpect(jsonPath("$[0].message").value("invalid UUID"));
        }

        @Test
        void shouldCallUsecaseOnValidInput() throws Exception {
            mockSuccess();
            final var input = new CreateAccountDTO(UUID.randomUUID().toString());
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));

            final var expectedInput = new CreateAccountUsecase.Input(UUID.fromString(input.clientId()));
            verify(createAccountUsecase, times(1)).execute(eq(expectedInput));
        }
    }
}