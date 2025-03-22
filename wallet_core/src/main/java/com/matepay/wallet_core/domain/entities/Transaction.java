package com.matepay.wallet_core.domain.entities;

import com.matepay.wallet_core.Exceptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class Transaction {
    private final UUID uuid;
    private final Account from;
    private final Account to;
    private final BigDecimal amount;
    private final Instant createdAt;

    public Transaction(Account from, Account to, BigDecimal amount) throws Exceptions {
        if (from == null) throw new Exceptions.AccountFromMustBePresent();
        if (to == null) throw new Exceptions.AccountToMustBePresent();
        if (amount == null) throw new Exceptions.AmountMustBePresent();
        if (amount.compareTo(BigDecimal.ZERO) == 0) throw new Exceptions.AmountMustBePresent();

        this.uuid = UUID.randomUUID();
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.createdAt = Instant.now();
    }

    public Transaction(UUID uuid, Account from, Account to, BigDecimal amount, Instant createdAt) throws Exceptions {
        if (from == null) throw new Exceptions.AccountFromMustBePresent();
        if (to == null) throw new Exceptions.AccountToMustBePresent();
        if (amount == null) throw new Exceptions.AmountMustBePresent();
        if (amount.compareTo(BigDecimal.ZERO) == 0) throw new Exceptions.AmountMustBePresent();

        this.uuid = uuid;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public void commit() {
        this.from.debit(this.amount);
        this.to.credit(this.amount);
    }
}
