package com.matepay.balances.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Account {
    private UUID uuid;
    private BigDecimal balance;
    private Instant updatedAt;
}
