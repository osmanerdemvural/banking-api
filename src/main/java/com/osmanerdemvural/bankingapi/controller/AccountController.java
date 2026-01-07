package com.osmanerdemvural.bankingapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.osmanerdemvural.bankingapi.dto.AccountCreateRequest;
import com.osmanerdemvural.bankingapi.dto.AccountResponse;
import com.osmanerdemvural.bankingapi.dto.AccountUpdateRequest;
import com.osmanerdemvural.bankingapi.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody AccountCreateRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public AccountResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    // /api/accounts?customerId=1
    @GetMapping
    public List<AccountResponse> list(@RequestParam(required = false) Long customerId) {
        return service.list(customerId);
    }

    @PutMapping("/{id}")
    public AccountResponse update(@PathVariable Long id, @Valid @RequestBody AccountUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void close(@PathVariable Long id) {
        service.close(id);
    }
}
