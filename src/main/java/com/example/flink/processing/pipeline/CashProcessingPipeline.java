package com.example.flink.processing.pipeline;

import com.example.flink.domain.model.Account;
import com.example.flink.domain.model.CashResult;
import com.example.flink.utils.Constants;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.LocalDateTime;

/**
 * Pipeline for processing cash positions and calculations from account data.
 * Implements business logic for cash position management and analysis.
 * Follows Flink best practices for stateful stream processing.
 */
public class CashProcessingPipeline {
    
    private final String pipelineName;
    private final long windowSizeMillis;
    
    public CashProcessingPipeline() {
        this("cash-processing-pipeline", Constants.DEFAULT_WINDOW_SIZE_MS); // Use constant from Constants
    }
    
    public CashProcessingPipeline(String pipelineName, long windowSizeMillis) {
        this.pipelineName = pipelineName;
        this.windowSizeMillis = windowSizeMillis;
    }
    
    /**
     * Processes account data to calculate cash positions and availability.
     * Groups accounts by ID and calculates cash metrics over time windows.
     * 
     * @param accountStream input stream of Account objects
     * @return DataStream of CashResult objects
     */
    public DataStream<CashResult> process(DataStream<Account> accountStream) {
        
        // Key accounts by account ID for individual cash position tracking
        KeyedStream<Account, String> keyedAccounts = accountStream
                .keyBy(new AccountIdKeySelector())
                .name("key-by-account-id")
                .uid("key-by-account-id-uid");
        
        // Apply windowing for time-based cash calculations
        DataStream<CashResult> cashResults = keyedAccounts
                .window(TumblingProcessingTimeWindows.of(Time.milliseconds(windowSizeMillis)))
                .aggregate(
                    new CashAggregator(),
                    new CashWindowFunction()
                )
                .name("cash-window-aggregation")
                .uid("cash-window-aggregation-uid");
        
        // Apply cash processing business rules
        return cashResults
                .map(new CashValidationFunction())
                .filter(result -> result != null && result.getStatus() != null)
                .name("cash-validation")
                .uid("cash-validation-uid");
    }
    
    /**
     * Alternative processing method with custom parallelism.
     * 
     * @param accountStream input stream of Account objects
     * @param parallelism desired parallelism level
     * @return DataStream of CashResult objects
     */
    public DataStream<CashResult> process(DataStream<Account> accountStream, int parallelism) {
        return process(accountStream)
                .setParallelism(parallelism);
    }
    
    /**
     * Key selector for grouping accounts by account ID.
     */
    private static class AccountIdKeySelector implements KeySelector<Account, String> {
        @Override
        public String getKey(Account account) throws Exception {
            return account.getAccountId() != null ? account.getAccountId() : "UNKNOWN";
        }
    }
    
    /**
     * Validation function for cash results.
     * Applies business rules and cash management policies.
     */
    private static class CashValidationFunction implements MapFunction<CashResult, CashResult> {
        @Override
        public CashResult map(CashResult result) throws Exception {
            if (result == null) {
                return null;
            }
            
            // Apply cash validation rules
            String status = Constants.STATUS_VALID;
            
            if (result.getCashPosition() < 0) {
                status = "NEGATIVE_CASH_POSITION";
            } else if (result.getAvailableCash() < 0) {
                status = "INVALID_AVAILABLE_CASH";
            } else if (result.getPendingAmount() < 0) {
                status = "INVALID_PENDING_AMOUNT";
            } else if (result.getCashPosition() < result.getAvailableCash()) {
                status = "INCONSISTENT_CASH_DATA";
            }
            
            return new CashResult(
                result.getAccountId(),
                result.getTransactionId(),
                result.getCashPosition(),
                result.getAvailableCash(),
                result.getPendingAmount(),
                result.getCurrency(),
                result.getCalculatedAt(),
                status
            );
        }
    }
    
    public String getPipelineName() {
        return pipelineName;
    }
    
    public long getWindowSizeMillis() {
        return windowSizeMillis;
    }
}
