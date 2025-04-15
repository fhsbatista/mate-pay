package com.matepay.balances.messaging.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceUpdated {
    public record Payload(
            UUID accountIdFrom,
            UUID accountIdTo,
            BigDecimal balanceAccountFrom,
            BigDecimal balanceAccountTo
    ) {
    }

    private static String NAME = "BalanceUpdated";
    private String name;
    private Instant dateTime;
    private Payload payload;
}
