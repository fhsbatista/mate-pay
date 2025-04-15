.PHONY: up down build logs clean

# Initialize all services
up:
	@echo "Starting infrastructure..."
	docker compose -f infra/compose.yml up -d
	@echo "Starting wallet-core..."
	docker compose -f wallet_core/compose.yml up -d
	@echo "Starting balances..."
	docker compose -f balances/compose.yml up -d

# Stop all services
down:
	@echo "Stopping balances..."
	docker compose -f balances/compose.yml down
	@echo "Stopping wallet-core..."
	docker compose -f wallet_core/compose.yml down
	@echo "Stopping infrastructure..."
	docker compose -f infra/compose.yml down

# Rebuild all services
build:
	@echo "Rebuilding infrastructure..."
	docker compose -f infra/compose.yml build
	@echo "Rebuilding wallet-core..."
	docker compose -f wallet_core/compose.yml build
	@echo "Rebuilding balances..."
	docker compose -f balances/compose.yml build

# Show logs from all services
logs:
	@echo "Infrastructure logs:"
	docker compose -f infra/compose.yml logs -f
	@echo "Wallet-core logs:"
	docker compose -f wallet_core/compose.yml logs -f
	@echo "Balances logs:"
	docker compose -f balances/compose.yml logs -f

# Clean all containers and volumes
clean:
	@echo "Cleaning all containers and volumes..."
	docker compose -f infra/compose.yml down -v
	docker compose -f wallet_core/compose.yml down -v
	docker compose -f balances/compose.yml down -v

# Clean only volumes
volumes:
	@echo "Cleaning volumes..."
	docker volume rm $$(docker volume ls -q) || true

# Restart all services
restart: down up

# Rebuild and restart all services
rebuild: build restart 