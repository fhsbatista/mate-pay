package com.matepay.wallet_core.domain.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class Account {
    private final UUID uuid;
    private final Client client;
    private BigDecimal balance;
    private final Instant createdAt;
    private Instant updatedAt;

    public Account(Client client) {
        if (client == null) throw new IllegalArgumentException("Client must be present");

        this.uuid = UUID.randomUUID();
        this.client = client;
        this.balance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Account(
            UUID uuid,
            Client client,
            BigDecimal balance,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.uuid = uuid;
        this.client = client;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void credit(BigDecimal amount) {
        this.balance = balance.add(amount);
        updatedAt = Instant.now();
    }

    public void debit(BigDecimal amount) {
        this.balance = balance.subtract(amount);
        updatedAt = Instant.now();
    }
}
