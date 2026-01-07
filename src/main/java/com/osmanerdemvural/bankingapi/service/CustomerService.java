package com.osmanerdemvural.bankingapi.service;

import com.osmanerdemvural.bankingapi.dto.CustomerCreateRequest;
import com.osmanerdemvural.bankingapi.dto.CustomerResponse;
import com.osmanerdemvural.bankingapi.entity.Customer;
import com.osmanerdemvural.bankingapi.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> list() {
        return customerRepository.findAll().stream()
                .map(c -> new CustomerResponse(c.getId(), c.getFullName(), c.getEmail(), c.getCreatedAt()))
                .toList();
    }

    public CustomerResponse create(CustomerCreateRequest req) {
        customerRepository.findByEmail(req.getEmail()).ifPresent(x -> {
            throw new IllegalArgumentException("Email already exists");
        });

        Customer c = new Customer();
        c.setFullName(req.getFullName());
        c.setEmail(req.getEmail());

        Customer saved = customerRepository.save(c);

        return new CustomerResponse(saved.getId(), saved.getFullName(), saved.getEmail(), saved.getCreatedAt());
    }
}
