package com.osmanerdemvural.bankingapi.dto;

import com.osmanerdemvural.bankingapi.entity.AccountStatus;

import jakarta.validation.constraints.NotNull;

public class AccountUpdateRequest {

    @NotNull
    private AccountStatus status;

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
}
