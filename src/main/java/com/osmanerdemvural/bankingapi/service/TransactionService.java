package com.osmanerdemvural.bankingapi.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osmanerdemvural.bankingapi.dto.TransactionResponse;
import com.osmanerdemvural.bankingapi.dto.TransferResponse;
import com.osmanerdemvural.bankingapi.entity.Account;
import com.osmanerdemvural.bankingapi.entity.AccountStatus;
import com.osmanerdemvural.bankingapi.entity.Transaction;
import com.osmanerdemvural.bankingapi.entity.TransactionType;
import com.osmanerdemvural.bankingapi.exception.NotFoundException;
import com.osmanerdemvural.bankingapi.repository.AccountRepository;
import com.osmanerdemvural.bankingapi.repository.TransactionRepository;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse deposit(Long accountId, BigDecimal amount, String description) {
        validateAmount(amount);

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));

        ensureActive(account);

        String cur = requireCurrency(account);

        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setType(TransactionType.DEPOSIT);
        tx.setAmount(amount);
        tx.setCurrency(cur); // auotomatic set
        tx.setDescription(normalizeDescription(description));
        tx.setBalanceAfter(newBalance);

        return toResponse(transactionRepository.save(tx));
    }

    @Transactional
    public TransactionResponse withdraw(Long accountId, BigDecimal amount, String description) {
        validateAmount(amount);

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));

        ensureActive(account);

        String cur = requireCurrency(account);

        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        account.setBalance(newBalance);

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setType(TransactionType.WITHDRAW);
        tx.setAmount(amount);
        tx.setCurrency(cur); // ✅ automatic set
        tx.setDescription(normalizeDescription(description));
        tx.setBalanceAfter(newBalance);

        return toResponse(transactionRepository.save(tx));
    }

    @Transactional
    public TransferResponse transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        validateAmount(amount);

        if (fromAccountId == null || toAccountId == null) {
            throw new IllegalArgumentException("fromAccountId and toAccountId are required");
        }
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("fromAccountId and toAccountId cannot be same");
        }

        // Reduce deadlock risk by always locking in same order
        Long first = Math.min(fromAccountId, toAccountId);
        Long second = Math.max(fromAccountId, toAccountId);

        Account a1 = accountRepository.findByIdForUpdate(first)
                .orElseThrow(() -> new NotFoundException("Account not found: " + first));
        Account a2 = accountRepository.findByIdForUpdate(second)
                .orElseThrow(() -> new NotFoundException("Account not found: " + second));

        Account from = fromAccountId.equals(a1.getId()) ? a1 : a2;
        Account to = toAccountId.equals(a1.getId()) ? a1 : a2;

        ensureActive(from);
        ensureActive(to);

        // ✅ transfer should be in same currency
        String curFrom = requireCurrency(from);
        String curTo = requireCurrency(to);

        if (!curFrom.equalsIgnoreCase(curTo)) {
            throw new IllegalArgumentException("Currency mismatch. Transfer only allowed within same currency.");
        }

        BigDecimal fromNew = from.getBalance().subtract(amount);
        if (fromNew.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        BigDecimal toNew = to.getBalance().add(amount);

        from.setBalance(fromNew);
        to.setBalance(toNew);

        UUID transferId = UUID.randomUUID();
        String desc = normalizeDescription(description);

        Transaction outTx = new Transaction();
        outTx.setAccount(from);
        outTx.setType(TransactionType.TRANSFER);
        outTx.setAmount(amount);
        outTx.setCurrency(curFrom);
        outTx.setDescription(desc != null ? desc : "Transfer out");
        outTx.setBalanceAfter(fromNew);
        outTx.setTransferId(transferId);

        Transaction inTx = new Transaction();
        inTx.setAccount(to);
        inTx.setType(TransactionType.TRANSFER);
        inTx.setAmount(amount);
        inTx.setCurrency(curTo);
        inTx.setDescription(desc != null ? desc : "Transfer in");
        inTx.setBalanceAfter(toNew);
        inTx.setTransferId(transferId);

        transactionRepository.save(outTx);
        transactionRepository.save(inTx);

        TransferResponse resp = new TransferResponse();
        resp.setTransferId(transferId);
        resp.setFromAccountId(fromAccountId);
        resp.setToAccountId(toAccountId);
        resp.setCurrency(curFrom);
        resp.setAmount(amount);
        resp.setFromBalanceAfter(fromNew);
        resp.setToBalanceAfter(toNew);

        return resp;
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> lastTransactions(Long accountId) {
        return transactionRepository.findTop50ByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // --- helpers ---

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }
        // istersen:
        // if (amount.scale() > 2) throw new IllegalArgumentException("Amount scale must be max 2 decimals");
    }

    private void ensureActive(Account account) {
        if (account.getStatus() == null || account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account is not ACTIVE");
        }
    }

    private String requireCurrency(Account account) {
        String cur = normalizeCurrency(account.getCurrency());
        if (cur == null) {
            throw new IllegalArgumentException("Account currency is missing");
        }
        return cur;
    }

    private String normalizeCurrency(String currency) {
        return currency == null ? null : currency.trim().toUpperCase();
    }

    private String normalizeDescription(String description) {
        if (description == null) return null;
        String d = description.trim();
        return d.isEmpty() ? null : d;
    }

    private TransactionResponse toResponse(Transaction tx) {
        TransactionResponse r = new TransactionResponse();
        r.setId(tx.getId());
        r.setAccountId(tx.getAccount().getId());
        r.setType(tx.getType());
        r.setAmount(tx.getAmount());
        r.setCurrency(tx.getCurrency());
        r.setDescription(tx.getDescription());
        r.setCreatedAt(tx.getCreatedAt());
        r.setBalanceAfter(tx.getBalanceAfter());
        r.setTransferId(tx.getTransferId());
        return r;
    }
}
