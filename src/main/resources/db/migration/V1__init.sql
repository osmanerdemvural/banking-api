-- V1__init.sql
-- SQL Server

CREATE TABLE dbo.customers (
  id          BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
  full_name   NVARCHAR(120) NOT NULL,
  email       NVARCHAR(255) NOT NULL,
  created_at  DATETIME2(7) NOT NULL CONSTRAINT DF_customers_created_at DEFAULT SYSUTCDATETIME(),
  updated_at  DATETIME2(7) NOT NULL CONSTRAINT DF_customers_updated_at DEFAULT SYSUTCDATETIME()
);

CREATE UNIQUE INDEX UX_customers_email ON dbo.customers(email);
