package com.matepay.wallet_core.messaging.events;

import com.matepay.events.Event;
import com.matepay.wallet_core.domain.entities.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
public class TransactionCreated implements Event {
    public record Payload(
            UUID transactionId,
            UUID accountFromId,
            UUID accountToId,
            Instant createdAt,
            BigDecimal amount) implements com.matepay.events.Payload {
    }

    private static final String NAME = "TransactionCreated";
    private final String name;
    private final Payload payload;
    private final Instant dateTime;

    public TransactionCreated(Transaction transaction) {
        this.name = NAME;
        this.dateTime = Instant.now();
        this.payload = new Payload(
                transaction.getUuid(),
                transaction.getFrom().getUuid(),
                transaction.getTo().getUuid(),
                transaction.getCreatedAt(),
                transaction.getAmount()
        );
    }
}
