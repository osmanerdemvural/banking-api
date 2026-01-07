package com.osmanerdemvural.bankingapi.entity;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "accounts", schema = "dbo",
        uniqueConstraints = @UniqueConstraint(name = "UX_accounts_account_number", columnNames = "account_number"))
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK -> customers.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_accounts_customer"))
    private Customer customer;

    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private AccountType type;

    // DB: VARCHAR(10) + CHECK LEN(currency)=3 => burada 3 yapıyoruz
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        this.createdAt = now;
        this.updatedAt = now;
        if (this.balance == null) this.balance = BigDecimal.ZERO;
        if (this.status == null) this.status = AccountStatus.ACTIVE;
        if (this.currency != null) this.currency = this.currency.trim().toUpperCase();
        if (this.accountNumber != null) this.accountNumber = this.accountNumber.trim();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now(Clock.systemUTC());
        if (this.currency != null) this.currency = this.currency.trim().toUpperCase();
        if (this.accountNumber != null) this.accountNumber = this.accountNumber.trim();
    }

    // getters/setters (IDE generate)
    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
