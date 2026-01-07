package com.osmanerdemvural.bankingapi.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferResponse {
    private UUID transferId;
    private Long fromAccountId;
    private Long toAccountId;
    private String currency;
    private BigDecimal amount;
    private BigDecimal fromBalanceAfter;
    private BigDecimal toBalanceAfter;

    // getters/setters
    public UUID getTransferId() { return transferId; }
    public void setTransferId(UUID transferId) { this.transferId = transferId; }

    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }

    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getFromBalanceAfter() { return fromBalanceAfter; }
    public void setFromBalanceAfter(BigDecimal fromBalanceAfter) { this.fromBalanceAfter = fromBalanceAfter; }

    public BigDecimal getToBalanceAfter() { return toBalanceAfter; }
    public void setToBalanceAfter(BigDecimal toBalanceAfter) { this.toBalanceAfter = toBalanceAfter; }
}
