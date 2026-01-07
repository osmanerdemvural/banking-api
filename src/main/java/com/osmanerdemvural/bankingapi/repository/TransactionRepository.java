package com.osmanerdemvural.bankingapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osmanerdemvural.bankingapi.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTop50ByAccountIdOrderByCreatedAtDesc(Long accountId);
}
