package com.osmanerdemvural.bankingapi.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.osmanerdemvural.bankingapi.dto.AccountCreateRequest;
import com.osmanerdemvural.bankingapi.dto.AccountResponse;
import com.osmanerdemvural.bankingapi.dto.AccountUpdateRequest;
import com.osmanerdemvural.bankingapi.entity.Account;
import com.osmanerdemvural.bankingapi.entity.Customer;
import com.osmanerdemvural.bankingapi.repository.AccountRepository;
import com.osmanerdemvural.bankingapi.repository.CustomerRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    public AccountResponse create(AccountCreateRequest req) {
        String accNo = req.getAccountNumber().trim();
        if (accountRepository.existsByAccountNumber(accNo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account number already exists");
        }

        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        Account a = new Account();
        a.setCustomer(customer);
        a.setAccountNumber(accNo);
        a.setType(req.getType());
        a.setCurrency(req.getCurrency());

        Account saved = accountRepository.save(a);
        return toResponse(saved);
    }

    public AccountResponse get(Long id) {
        Account a = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return toResponse(a);
    }

    public List<AccountResponse> list(Long customerId) {
        if (customerId == null) {
            return accountRepository.findAll().stream().map(this::toResponse).toList();
        }
        return accountRepository.findByCustomerId(customerId).stream().map(this::toResponse).toList();
    }

    public AccountResponse update(Long id, AccountUpdateRequest req) {
        Account a = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        a.setStatus(req.getStatus());
        return toResponse(accountRepository.save(a));
    }

    public void close(Long id) {
        Account a = accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        a.setStatus(com.osmanerdemvural.bankingapi.entity.AccountStatus.CLOSED);
        accountRepository.save(a);
    }

    private AccountResponse toResponse(Account a) {
        AccountResponse r = new AccountResponse();
        r.setId(a.getId());
        r.setCustomerId(a.getCustomer().getId());
        r.setAccountNumber(a.getAccountNumber());
        r.setType(a.getType());
        r.setCurrency(a.getCurrency());
        r.setBalance(a.getBalance());
        r.setStatus(a.getStatus());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedAt(a.getUpdatedAt());
        return r;
    }
}
