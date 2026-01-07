# Banking API + UI (Spring Boot + React)

A full-stack demo banking application:
- **Spring Boot REST API** (Customers, Accounts, Transactions)
- **React + Vite UI** (basic management UI)
- **PostgreSQL + Flyway** for DB migrations
- **Docker Compose** for easy local setup

## Features
### Backend (API)
- Customers: list, create
- Accounts: create, get, list by customer, update, close
- Transactions:
  - Deposit
  - Withdraw
  - Transfer (creates 2 transaction records linked by transferId)
  - List last transactions for an account
- Global exception handling with consistent error response

### Frontend (UI)
- Create & list customers
- Create & list accounts (by selected customer)
- View account detail and transactions
- Basic error display

## Tech Stack
- Java 17+ (Spring Boot)
- PostgreSQL
- Flyway
- React + TypeScript
- Vite
- Axios
- Docker Compose

---

## Project Structure
.
├── src/ # Spring Boot backend
├── banking-ui/ # React frontend (Vite)
└── docker-compose.yml # PostgreSQL + app support

yaml
Copy code

---

## Run with Docker (Recommended)
> Starts PostgreSQL for local development.

1) Start DB:
```bash
docker compose up -d
Run backend:

bash
Copy code
./mvnw spring-boot:run
Backend will run on:

http://localhost:8081

Run frontend:

bash
Copy code
cd banking-ui
npm install
npm run dev
Frontend will run on:

http://localhost:5173

Environment Variables
Frontend
Create banking-ui/.env based on .env.example:

env
Copy code
VITE_API_BASE_URL=http://localhost:8081
The frontend uses VITE_API_BASE_URL and appends /api internally.

API Endpoints (Quick)
Base URL:

http://localhost:8081/api

Customers:

GET /customers

POST /customers

Accounts:

POST /accounts

GET /accounts?customerId=1

GET /accounts/{id}

Transactions:

POST /accounts/{id}/deposit

POST /accounts/{id}/withdraw

POST /accounts/transfer

GET /accounts/{id}/transactions

Quick Test (PowerShell)
Create customer:

powershell
Copy code
$body = @{ fullName = "Test User"; email = "test1@example.com" } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8081/api/customers" -ContentType "application/json" -Body $body
Create account:

powershell
Copy code
$body = @{ customerId = 1; accountNumber = "ACC-0001"; type = "CHECKING"; currency = "CAD" } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8081/api/accounts" -ContentType "application/json" -Body $body
Deposit:

powershell
Copy code
$body = @{ amount = 100.00; description = "cash" } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8081/api/accounts/1/deposit" -ContentType "application/json" -Body $body
Next Improvements (Portfolio)
Add Swagger/OpenAPI UI

Add unit tests + integration tests (Testcontainers)

Add frontend polish (routing, UI kit, better UX)

Add GitHub Actions CI (build + tests)

Add screenshots + short demo GIF

License
MIT