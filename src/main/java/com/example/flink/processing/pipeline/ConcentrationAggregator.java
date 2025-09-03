package com.example.flink.processing.pipeline;

import com.example.flink.domain.model.Account;
import com.example.flink.domain.model.ConcentrationResult;
import com.example.flink.utils.Constants;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;

/**
 * Technical aggregator for accumulating concentration-related data within time windows.
 * Pure aggregation without business logic - business rules should be applied in separate processors.
 * 
 * This aggregator only:
 * - Accumulates balances by account type
 * - Counts accounts
 * - Tracks timing information
 * 
 * Business logic (risk calculations, thresholds, validations) should be implemented in dedicated processors.
 */
public class ConcentrationAggregator implements AggregateFunction<Account, ConcentrationAggregator.ConcentrationAccumulator, ConcentrationResult> {
    
    /**
     * Accumulator class for maintaining aggregation state.
     */
    public static class ConcentrationAccumulator {
        public String accountType;
        public double totalBalance;
        public int accountCount;
        public double maxBalance;
        public double minBalance;
        
        public ConcentrationAccumulator() {
            this.totalBalance = 0.0;
            this.accountCount = 0;
            this.maxBalance = Double.MIN_VALUE;
            this.minBalance = Double.MAX_VALUE;
        }
    }
    
    @Override
    public ConcentrationAccumulator createAccumulator() {
        return new ConcentrationAccumulator();
    }
    
    @Override
    public ConcentrationAccumulator add(Account account, ConcentrationAccumulator accumulator) {
        if (account == null) {
            return accumulator;
        }
        
        // Set account type if not already set
        if (accumulator.accountType == null) {
            accumulator.accountType = account.getAccountType();
        }
        
        // Update aggregation values
        accumulator.totalBalance += account.getBalance();
        accumulator.accountCount++;
        accumulator.maxBalance = Math.max(accumulator.maxBalance, account.getBalance());
        accumulator.minBalance = Math.min(accumulator.minBalance, account.getBalance());
        
        return accumulator;
    }
    
    @Override
    public ConcentrationResult getResult(ConcentrationAccumulator accumulator) {
        if (accumulator.accountCount == 0) {
            return new ConcentrationResult(
                "EMPTY",
                accumulator.accountType,
                0.0,
                "AGGREGATION",
                0.0,
                0.0,
                LocalDateTime.now(),
                "NO_DATA"
            );
        }
        
        // Pure aggregation result - no business logic applied
        double averageBalance = accumulator.totalBalance / accumulator.accountCount;
        
        return new ConcentrationResult(
            accumulator.accountType + "_CONCENTRATION",
            accumulator.accountType,
            averageBalance,           // Raw average (business logic to be applied later)
            "AGGREGATION",
            accumulator.totalBalance,
            1.0,                     // Default weight (risk calculation to be done by business processor)
            LocalDateTime.now(),
            "AGGREGATED"
        );
    }
    
    @Override
    public ConcentrationAccumulator merge(ConcentrationAccumulator acc1, ConcentrationAccumulator acc2) {
        ConcentrationAccumulator merged = new ConcentrationAccumulator();
        
        merged.accountType = acc1.accountType != null ? acc1.accountType : acc2.accountType;
        merged.totalBalance = acc1.totalBalance + acc2.totalBalance;
        merged.accountCount = acc1.accountCount + acc2.accountCount;
        merged.maxBalance = Math.max(acc1.maxBalance, acc2.maxBalance);
        merged.minBalance = Math.min(acc1.minBalance, acc2.minBalance);
        
        return merged;
    }
    
    /**
     * Calculate concentration ratio based on balance distribution.
     * Higher values indicate higher concentration risk.
     */
    private double calculateConcentrationRatio(ConcentrationAccumulator accumulator) {
        if (accumulator.accountCount <= 1 || accumulator.totalBalance == 0) {
            return 1.0; // Maximum concentration for single account or zero balance
        }
}

/**
 * Window function for finalizing concentration aggregations with window metadata.
 */
class ConcentrationWindowFunction implements WindowFunction<ConcentrationResult, ConcentrationResult, String, TimeWindow> {
    
    @Override
    public void apply(String key, TimeWindow window, Iterable<ConcentrationResult> input, Collector<ConcentrationResult> out) throws Exception {
        for (ConcentrationResult result : input) {
            // Add window metadata to the result
            ConcentrationResult enrichedResult = new ConcentrationResult(
                result.getEntityId() + "_WINDOW_" + window.getStart(),
                result.getEntityType(),
                result.getConcentrationValue(),
                result.getCalculationMethod() + "_WINDOWED",
                result.getTotalExposure(),
                result.getRiskWeight(),
                result.getCalculatedAt(),
                result.getStatus()
            );
            
            out.collect(enrichedResult);
        }
    }
}
