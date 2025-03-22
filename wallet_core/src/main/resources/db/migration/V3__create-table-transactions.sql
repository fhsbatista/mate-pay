CREATE TABLE transactions(
    id BINARY(16) NOT NULL,
    account_from_id BINARY(16) NOT NULL,
    account_to_id BINARY(16) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    created_at DATETIME(6) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (account_from_id) REFERENCES accounts(id),
    FOREIGN KEY (account_to_id) REFERENCES accounts(id)
)