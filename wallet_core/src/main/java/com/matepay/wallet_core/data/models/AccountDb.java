package com.matepay.wallet_core.data.models;

import com.matepay.wallet_core.domain.entities.Account;
import com.matepay.wallet_core.domain.entities.Client;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table(name = "accounts")
@Entity(name = "account")
@Getter
@EqualsAndHashCode
public class AccountDb {
    @Id
    private UUID id;
    private BigDecimal balance;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientDb client;

    public AccountDb() {
    }

    public AccountDb(
            UUID id,
            ClientDb client,
            BigDecimal balance,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.client = client;
        this.balance = balance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AccountDb fromDomain(Account account) {
        return new AccountDb(
                account.getUuid(),
                ClientDb.fromDomainWithNoAccounts(account.getClient()),
                account.getBalance(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }

    public Account toDomain() {
        return new Account(
                id,
                client.toDomainWithNoAccounts(),
                balance,
                createdAt,
                updatedAt
        );
    }

    public Account toDomain(Client client) {
        return new Account(
                id,
                client,
                balance,
                createdAt,
                updatedAt
        );
    }
}
