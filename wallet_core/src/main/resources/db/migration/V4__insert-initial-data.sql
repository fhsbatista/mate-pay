-- Insert clients
INSERT INTO clients (id, name, email, created_at, updated_at) VALUES
(UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')), 'John Doe', 'john@example.com', NOW(), NOW()),
(UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')), 'Jane Smith', 'jane@example.com', NOW(), NOW());

-- Insert accounts for clients
INSERT INTO accounts (id, client_id, balance, created_at, updated_at) VALUES
(UNHEX(REPLACE('33333333-3333-3333-3333-333333333333', '-', '')), UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')), 1000.00, NOW(), NOW()),
(UNHEX(REPLACE('44444444-4444-4444-4444-444444444444', '-', '')), UNHEX(REPLACE('22222222-2222-2222-2222-222222222222', '-', '')), 2000.00, NOW(), NOW()); 