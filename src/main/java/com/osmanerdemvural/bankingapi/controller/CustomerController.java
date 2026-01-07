package com.osmanerdemvural.bankingapi.controller;

import com.osmanerdemvural.bankingapi.dto.CustomerCreateRequest;
import com.osmanerdemvural.bankingapi.dto.CustomerResponse;
import com.osmanerdemvural.bankingapi.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponse> list() {
        return customerService.list();
    }

    @PostMapping
    public CustomerResponse create(@Valid @RequestBody CustomerCreateRequest req) {
        return customerService.create(req);
    }
}
