package com.matepay.balances.data.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.matepay.balances.domain.entities.Account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Table(name = "accounts")
@Entity(name = "account")
@Getter
@EqualsAndHashCode
public class AccountDb {
    @Id
    private UUID id;
    private BigDecimal balance;
    private Instant updatedAt;

    public AccountDb() {
    }

    public AccountDb(
            UUID id,
            BigDecimal balance,
            Instant updatedAt
    ) {
        this.id = id;
        this.balance = balance;
        this.updatedAt = updatedAt;
    }

    public static AccountDb fromDomain(Account account) {
        return new AccountDb(
                account.getUuid(),
                account.getBalance(),
                account.getUpdatedAt()
        );
    }

    public Account toDomain() {
        return new Account(
                id,
                balance,
                updatedAt
        );
    }
}
