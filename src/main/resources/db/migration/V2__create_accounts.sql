-- V2__create_accounts.sql
-- SQL Server

CREATE TABLE dbo.accounts (
  id             BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
  customer_id    BIGINT NOT NULL,
  account_number VARCHAR(20) NOT NULL,
  type           VARCHAR(20) NOT NULL,      -- CHECKING/SAVINGS
  currency       VARCHAR(3)  NOT NULL,      -- CAD/USD (3 char)
  balance        DECIMAL(19,2) NOT NULL CONSTRAINT DF_accounts_balance DEFAULT (0),
  status         VARCHAR(20) NOT NULL CONSTRAINT DF_accounts_status DEFAULT ('ACTIVE'),
  created_at     DATETIME2(7) NOT NULL CONSTRAINT DF_accounts_created_at DEFAULT SYSUTCDATETIME(),
  updated_at     DATETIME2(7) NOT NULL CONSTRAINT DF_accounts_updated_at DEFAULT SYSUTCDATETIME(),

  CONSTRAINT FK_accounts_customer
    FOREIGN KEY (customer_id) REFERENCES dbo.customers(id),

  CONSTRAINT CK_accounts_balance_nonneg CHECK (balance >= 0),
  CONSTRAINT CK_accounts_currency_len CHECK (LEN(currency) = 3),
  CONSTRAINT CK_accounts_type CHECK (type IN ('CHECKING','SAVINGS')),
  CONSTRAINT CK_accounts_status CHECK (status IN ('ACTIVE','CLOSED'))
);

CREATE UNIQUE INDEX UX_accounts_account_number ON dbo.accounts(account_number);
CREATE INDEX IX_accounts_customer_id ON dbo.accounts(customer_id);
