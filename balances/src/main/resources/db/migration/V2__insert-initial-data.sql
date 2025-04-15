-- Insert initial balances for John Doe and Jane Smith accounts
INSERT INTO accounts (id, balance, updated_at) VALUES
(UNHEX(REPLACE('33333333-3333-3333-3333-333333333333', '-', '')), 1000.00, NOW()),
(UNHEX(REPLACE('44444444-4444-4444-4444-444444444444', '-', '')), 2000.00, NOW()); 