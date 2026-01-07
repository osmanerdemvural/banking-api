-- V3__create_transactions.sql
-- SQL Server

CREATE TABLE dbo.transactions (
  id            BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
  account_id    BIGINT NOT NULL,
  type          VARCHAR(20) NOT NULL,       -- DEPOSIT/WITHDRAW/TRANSFER
  amount        DECIMAL(19,2) NOT NULL,
  currency      VARCHAR(3) NOT NULL,
  description   NVARCHAR(255) NULL,
  created_at    DATETIME2(7) NOT NULL CONSTRAINT DF_transactions_created_at DEFAULT SYSUTCDATETIME(),
  balance_after DECIMAL(19,2) NOT NULL,
  transfer_id   UNIQUEIDENTIFIER NULL,

  CONSTRAINT FK_transactions_account
    FOREIGN KEY (account_id) REFERENCES dbo.accounts(id),

  CONSTRAINT CK_transactions_amount_pos CHECK (amount > 0),
  CONSTRAINT CK_transactions_currency_len CHECK (LEN(currency) = 3),
  CONSTRAINT CK_transactions_type CHECK (type IN ('DEPOSIT','WITHDRAW','TRANSFER'))
);

CREATE INDEX IX_transactions_account_id_created_at ON dbo.transactions(account_id, created_at DESC);
CREATE INDEX IX_transactions_transfer_id ON dbo.transactions(transfer_id);
