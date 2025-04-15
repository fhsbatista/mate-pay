package com.matepay.balances.messaging;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.matepay.balances.domain.Exceptions;
import com.matepay.balances.domain.usecases.CreateAccountUsecase;
import com.matepay.balances.domain.usecases.RegisterBalanceUpdatedUsecase;

@Component
public class BalanceUpdatedConsumer {

    @Autowired
    private RegisterBalanceUpdatedUsecase registerBalanceUpdatedUsecase;

    @Autowired
    private CreateAccountUsecase createAccountUsecase;

    @KafkaListener(topics = "balances", groupId = "balances-group")
    public void consume(BalanceUpdated event) {
        updateBalance(event.getPayload().accountIdFrom(), event.getPayload().balanceAccountFrom());
        updateBalance(event.getPayload().accountIdTo(), event.getPayload().balanceAccountTo());
    }

    private void updateBalance(UUID accountID, BigDecimal updatedBalance) {
        final var input = new RegisterBalanceUpdatedUsecase.Input(accountID, updatedBalance);
        try {
            registerBalanceUpdatedUsecase.execute(input);
        } catch (Exceptions.AccountNotFound e) {
            final var createInput = new CreateAccountUsecase.Input(accountID, updatedBalance);
            createAccountUsecase.execute(createInput);
        }
    }
}
