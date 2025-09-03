package com.example.flink.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data model representing processed cash calculation results.
 * This is the output of the cash processing pipeline.
 */
public class CashResult {
    
    private final String accountId;
    private final String transactionId;
    private final double cashPosition;
    private final double availableCash;
    private final double pendingAmount;
    private final String currency;
    private final LocalDateTime calculatedAt;
    private final String status;
    
    public CashResult() {
        this(null, null, 0.0, 0.0, 0.0, null, null, null);
    }
    
    public CashResult(String accountId, String transactionId, double cashPosition,
                     double availableCash, double pendingAmount, String currency,
                     LocalDateTime calculatedAt, String status) {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.cashPosition = cashPosition;
        this.availableCash = availableCash;
        this.pendingAmount = pendingAmount;
        this.currency = currency;
        this.calculatedAt = calculatedAt;
        this.status = status;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public double getCashPosition() {
        return cashPosition;
    }
    
    public double getAvailableCash() {
        return availableCash;
    }
    
    public double getPendingAmount() {
        return pendingAmount;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashResult that = (CashResult) o;
        return Double.compare(that.cashPosition, cashPosition) == 0 &&
               Double.compare(that.availableCash, availableCash) == 0 &&
               Double.compare(that.pendingAmount, pendingAmount) == 0 &&
               Objects.equals(accountId, that.accountId) &&
               Objects.equals(transactionId, that.transactionId) &&
               Objects.equals(currency, that.currency) &&
               Objects.equals(calculatedAt, that.calculatedAt) &&
               Objects.equals(status, that.status);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId, transactionId, cashPosition, availableCash, 
                           pendingAmount, currency, calculatedAt, status);
    }
    
    @Override
    public String toString() {
        return "CashResult{" +
               "accountId='" + accountId + '\'' +
               ", transactionId='" + transactionId + '\'' +
               ", cashPosition=" + cashPosition +
               ", availableCash=" + availableCash +
               ", pendingAmount=" + pendingAmount +
               ", currency='" + currency + '\'' +
               ", calculatedAt=" + calculatedAt +
               ", status='" + status + '\'' +
               '}';
    }
}
