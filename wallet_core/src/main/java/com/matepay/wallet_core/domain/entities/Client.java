package com.matepay.wallet_core.domain.entities;

import com.matepay.wallet_core.Exceptions;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Client {
    private final UUID uuid;
    private final String name;
    private final String email;
    private List<Account> accounts;
    private final Instant createdAt;
    private final Instant updatedA;

    public Client(String name, String email) {
        if (!isPresent(name)) throw new IllegalArgumentException("Name must be present");
        if (!isPresent(email)) throw new IllegalArgumentException("Email must be present");

        this.uuid = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.accounts = List.of();
        this.createdAt = Instant.now();
        this.updatedA = Instant.now();
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