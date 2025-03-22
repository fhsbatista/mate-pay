package com.matepay.wallet_core.data.models;

import com.matepay.wallet_core.domain.entities.Client;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Table(name = "clients")
@Entity(name = "client")
@Getter
@EqualsAndHashCode
public class ClientDb {
    @Id
    private UUID id;
    private String name;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;

    @Setter
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountDb> accounts;

    public ClientDb() {
    }

    public ClientDb(
            UUID id,
            String name,
            String email,
            List<AccountDb> accounts,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.accounts = accounts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ClientDb fromDomainWithNoAccounts(Client client) {
        return new ClientDb(
                client.getUuid(),
                client.getName(),
                client.getEmail(),
                List.of(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }

    public Client toDomainWithNoAccounts() {
        return new Client(
                id,
                name,
                email,
                List.of(),
                createdAt,
                updatedAt
        );
    }

}
