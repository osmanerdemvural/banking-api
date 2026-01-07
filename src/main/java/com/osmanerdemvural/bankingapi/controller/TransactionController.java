package com.osmanerdemvural.bankingapi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osmanerdemvural.bankingapi.dto.MoneyRequest;
import com.osmanerdemvural.bankingapi.dto.TransactionResponse;
import com.osmanerdemvural.bankingapi.dto.TransferRequest;
import com.osmanerdemvural.bankingapi.dto.TransferResponse;
import com.osmanerdemvural.bankingapi.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{id}/deposit")
    public TransactionResponse deposit(
            @PathVariable("id") Long id,
            @Valid @RequestBody MoneyRequest req
    ) {
        return transactionService.deposit(id, req.getAmount(), req.getDescription());
    }

    @PostMapping("/{id}/withdraw")
    public TransactionResponse withdraw(
            @PathVariable("id") Long id,
            @Valid @RequestBody MoneyRequest req
    ) {
        return transactionService.withdraw(id, req.getAmount(), req.getDescription());
    }

    @PostMapping("/transfer")
    public TransferResponse transfer(@Valid @RequestBody TransferRequest req) {
        return transactionService.transfer(
                req.getFromAccountId(),
                req.getToAccountId(),
                req.getAmount(),
                req.getDescription()
        );
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionResponse> transactions(@PathVariable("id") Long id) {
        return transactionService.lastTransactions(id);
    }
}
