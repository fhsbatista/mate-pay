package com.matepay.wallet_core.domain.entities;

import com.matepay.wallet_core.Exceptions;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
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
        if (!hasBalance(from, amount)) throw new Exceptions.NotEnoughBalance();

        this.uuid = UUID.randomUUID();
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.createdAt = Instant.now();
    }

    public void commit() {
        this.from.debit(this.amount);
        this.to.credit(this.amount);
    }

    private boolean hasBalance(Account from, BigDecimal amount) {
        return from.getBalance().compareTo(amount) >= 0;
    }
}
