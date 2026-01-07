package com.osmanerdemvural.bankingapi.dto;

import com.osmanerdemvural.bankingapi.entity.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AccountCreateRequest {

    @NotNull
    private Long customerId;

    @NotBlank
    @Size(max = 20)
    private String accountNumber;

    @NotNull
    private AccountType type;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
