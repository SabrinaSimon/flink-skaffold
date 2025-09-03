package com.example.flink.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data model representing an account record from source files.
 * This class follows immutable design pattern as recommended for Flink data types.
 */
public class Account {
    
    private final String accountId;
    private final String accountName;
    private final String accountType;
    private final double balance;
    private final String currency;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    
    public Account() {
        this(null, null, null, 0.0, null, null, null);
    }
    
    public Account(String accountId, String accountName, String accountType, 
                   double balance, String currency, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public String getAccountName() {
        return accountName;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Double.compare(account.balance, balance) == 0 &&
               Objects.equals(accountId, account.accountId) &&
               Objects.equals(accountName, account.accountName) &&
               Objects.equals(accountType, account.accountType) &&
               Objects.equals(currency, account.currency) &&
               Objects.equals(createdAt, account.createdAt) &&
               Objects.equals(updatedAt, account.updatedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountName, accountType, balance, currency, createdAt, updatedAt);
    }
    
    @Override
    public String toString() {
        return "Account{" +
               "accountId='" + accountId + '\'' +
               ", accountName='" + accountName + '\'' +
               ", accountType='" + accountType + '\'' +
               ", balance=" + balance +
               ", currency='" + currency + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}
