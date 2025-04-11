package com.matepay.wallet_core.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matepay.wallet_core.domain.entities.Client;
import com.matepay.wallet_core.domain.usecases.CreateClientUsecase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientsController.class)
class ClientsControllerTest {
    @MockitoBean
    private CreateClientUsecase createClientUsecase;

    @Autowired
    @InjectMocks
    private ClientsController sut;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockSuccess();
        mockMvc = MockMvcBuilders
                .standaloneSetup(sut)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    void mockSuccess() {
        final var client = new Client("mock", "mock");
        when(createClientUsecase.execute(any())).thenReturn(client);
    }

    @Nested
    @DisplayName("Create")
    class CreateTests {
        @Test
        void shouldReturnErrorOnInvalidName() throws Exception {
            final var input = new CreateClientDTO("", "valid_mail@mail.com");
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("name"))
                    .andExpect(jsonPath("$[0].message").value("must not be blank"));
        }

        @Test
        void shouldReturnErrorOnInvalidEmail() throws Exception {
            final var input = new CreateClientDTO("Valid name", "invalidmail.com");
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$[0].field").value("email"))
                    .andExpect(jsonPath("$[0].message").value("must be a well-formed email address"));
        }

        @Test
        void shouldCallUsecaseOnValidInput() throws Exception {
            mockSuccess();
            final var input = new CreateClientDTO("Valid name", "valid@mail.com");
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json));

            final var expectedInput = new CreateClientUsecase.Input(input.name(), input.email());
            verify(createClientUsecase, times(1)).execute(eq(expectedInput));
        }


        @Test
        void shouldReturn200OnUsecaseSuccess() throws Exception {
            mockSuccess();
            final var input = new CreateClientDTO("Valid name", "valid@mail.com");
            final var json = new ObjectMapper().writeValueAsString(input);

            mockMvc.perform(post("/api/clients")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().is2xxSuccessful());
        }
    }
}