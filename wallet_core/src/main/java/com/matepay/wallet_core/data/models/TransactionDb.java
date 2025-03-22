package com.matepay.wallet_core.data.models;

import com.matepay.wallet_core.Exceptions;
import com.matepay.wallet_core.domain.entities.Transaction;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table(name = "transactions")
@Entity(name = "transaction")
@Getter
@EqualsAndHashCode
public class TransactionDb {
    @Id
    private UUID uuid;
    private BigDecimal amount;
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "account_from_id")
    private AccountDb from;

    @ManyToOne
    @JoinColumn(name = "account_to_id")
    private AccountDb to;

    public TransactionDb() {
    }

    public TransactionDb(
            UUID uuid,
            AccountDb from,
            AccountDb to,
            BigDecimal amount,
            Instant createdAt
    ) {
        this.uuid = uuid;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static TransactionDb fromDomain(Transaction transaction) {
        return new TransactionDb(
                transaction.getUuid(),
                AccountDb.fromDomain(transaction.getFrom()),
                AccountDb.fromDomain(transaction.getTo()),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );
    }

    public Transaction toDomain() throws Exceptions {
        return new Transaction(uuid, from.toDomain(), to.toDomain(), amount, createdAt);
    }
}
