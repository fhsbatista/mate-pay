package com.matepay.wallet_core.domain.entities;

import com.matepay.wallet_core.Exceptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class Client {
    private final UUID uuid;
    private final String name;
    private final String email;
    private List<Account> accounts;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Client(String name, String email) {
        if (!isPresent(name)) throw new IllegalArgumentException("Name must be present");
        if (!isPresent(email)) throw new IllegalArgumentException("Email must be present");

        this.uuid = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.accounts = List.of();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Client(
            UUID uuid,
            String name,
            String email,
            List<Account> accounts,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.accounts = accounts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }

    public void addAccount(Account account) throws Exceptions.AccountAlreadyBelongsToOtherClient {
        if (account.getClient().getUuid() != this.uuid) {
            throw new Exceptions.AccountAlreadyBelongsToOtherClient();
        }

        List<Account> accounts = new ArrayList<>(this.accounts);
        accounts.add(account);

        this.accounts = Collections.unmodifiableList(accounts);
    }
}