package com.matepay.wallet_core.messaging.events;

import com.matepay.events.Event;
import com.matepay.events.Payload;
import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BalanceUpdated implements Event {
    public record Payload(
            UUID accountIdFrom,
            UUID accountIdTo,
            BigDecimal balanceAccountFrom,
            BigDecimal balanceAccountTo
    ) implements com.matepay.events.Payload {
    }

    private static String NAME = "BalanceUpdated";

    private final String name;
    private final Payload payload;
    private final Instant dateTime;

    public BalanceUpdated(Account from, Account to) {
        this.name = NAME;
        this.dateTime = Instant.now();
        this.payload = new Payload(
                from.getUuid(),
                to.getUuid(),
                from.getBalance(),
                to.getBalance()
        );
    }
}
