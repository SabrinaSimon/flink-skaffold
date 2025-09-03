package com.example.flink.processing.pipeline;

import com.example.flink.domain.model.Account;
import com.example.flink.domain.model.CashResult;
import com.example.flink.utils.Constants;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Technical aggregator for accumulating cash-related data within time windows.
 * Pure aggregation without business logic - business rules should be applied in separate processors.
 * 
 * This aggregator only:
 * - Accumulates account balances
 * - Counts transactions
 * - Tracks timing information
 * 
 * Business logic (validation, calculations, rules) should be implemented in dedicated processors.
 */
public class CashAggregator implements AggregateFunction<Account, CashAggregator.CashAccumulator, CashResult> {
    
    /**
     * Accumulator class for maintaining cash aggregation state.
     */
    public static class CashAccumulator {
        public String accountId;
        public String currency;
        public double totalBalance;
        public double maxBalance;
        public double minBalance;
        public int transactionCount;
        public LocalDateTime lastUpdateTime;
        
        public CashAccumulator() {
            this.totalBalance = 0.0;
            this.maxBalance = Double.MIN_VALUE;
            this.minBalance = Double.MAX_VALUE;
            this.transactionCount = 0;
            this.lastUpdateTime = LocalDateTime.now();
        }
    }
    
    @Override
    public CashAccumulator createAccumulator() {
        return new CashAccumulator();
    }
    
    @Override
    public CashAccumulator add(Account account, CashAccumulator accumulator) {
        if (account == null) {
            return accumulator;
        }
        
        // Set account details if not already set
        if (accumulator.accountId == null) {
            accumulator.accountId = account.getAccountId();
            accumulator.currency = account.getCurrency();
        }
        
        // Update cash metrics
        accumulator.totalBalance = account.getBalance(); // Use latest balance as current position
        accumulator.maxBalance = Math.max(accumulator.maxBalance, account.getBalance());
        accumulator.minBalance = Math.min(accumulator.minBalance, account.getBalance());
        accumulator.transactionCount++;
        accumulator.lastUpdateTime = account.getUpdatedAt() != null ? 
                                   account.getUpdatedAt() : LocalDateTime.now();
        
        return accumulator;
    }
    
    @Override
    public CashResult getResult(CashAccumulator accumulator) {
        if (accumulator.transactionCount == 0 || accumulator.accountId == null) {
            return new CashResult(
                "UNKNOWN",
                UUID.randomUUID().toString(),
                0.0,
                0.0,
                0.0,
                Constants.DEFAULT_CURRENCY,
                LocalDateTime.now(),
                "NO_DATA"
            );
        }
        
        // Pure aggregation result - no business logic applied
        return new CashResult(
            accumulator.accountId,
            generateTransactionId(accumulator),
            accumulator.totalBalance,  // Raw total balance
            accumulator.totalBalance,  // Raw available (business logic to be applied later)
            0.0,                      // Pending amount (to be calculated by business processor)
            accumulator.currency != null ? accumulator.currency : Constants.DEFAULT_CURRENCY,
            accumulator.lastUpdateTime,
            "AGGREGATED"
        );
    }
    
    @Override
    public CashAccumulator merge(CashAccumulator acc1, CashAccumulator acc2) {
        CashAccumulator merged = new CashAccumulator();
        
        // Use the most recent account information
        if (acc2.lastUpdateTime.isAfter(acc1.lastUpdateTime)) {
            merged.accountId = acc2.accountId;
            merged.currency = acc2.currency;
            merged.totalBalance = acc2.totalBalance;
            merged.lastUpdateTime = acc2.lastUpdateTime;
        } else {
            merged.accountId = acc1.accountId;
            merged.currency = acc1.currency;
            merged.totalBalance = acc1.totalBalance;
            merged.lastUpdateTime = acc1.lastUpdateTime;
        }
        
        merged.maxBalance = Math.max(acc1.maxBalance, acc2.maxBalance);
        merged.minBalance = Math.min(acc1.minBalance, acc2.minBalance);
        merged.transactionCount = acc1.transactionCount + acc2.transactionCount;
        
        return merged;
    }
    
    /**
     * Generate a unique transaction ID for the cash aggregation.
     */
    private String generateTransactionId(CashAccumulator accumulator) {
        return "CASH_AGG_" + accumulator.accountId + "_" + 
               accumulator.lastUpdateTime.toString().replace(":", "").replace("-", "");
    }
}

/**
 * Window function for finalizing cash calculations with window metadata.
 */
class CashWindowFunction implements WindowFunction<CashResult, CashResult, String, TimeWindow> {
    
    @Override
    public void apply(String key, TimeWindow window, Iterable<CashResult> input, Collector<CashResult> out) throws Exception {
        for (CashResult result : input) {
            // Add window timing information to transaction ID
            String windowedTransactionId = result.getTransactionId() + "_W" + window.getStart();
            
            CashResult enrichedResult = new CashResult(
                result.getAccountId(),
                windowedTransactionId,
                result.getCashPosition(),
                result.getAvailableCash(),
                result.getPendingAmount(),
                result.getCurrency(),
                result.getCalculatedAt(),
                result.getStatus()
            );
            
            out.collect(enrichedResult);
        }
    }
}
