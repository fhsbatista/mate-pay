CREATE TABLE accounts(
    id BINARY(16) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    PRIMARY KEY (id)
)