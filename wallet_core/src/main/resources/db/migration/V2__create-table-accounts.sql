CREATE TABLE accounts(
    id BINARY(16) NOT NULL,
    client_id BINARY(16) NOT NULL,
    balance DECIMAL(15,2) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients(id)
)