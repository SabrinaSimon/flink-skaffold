package com.example.flink.processing.pipeline;

import com.example.flink.domain.model.Account;
import com.example.flink.domain.model.ConcentrationResult;
import com.example.flink.utils.Constants;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Pipeline for calculating concentration metrics from account data.
 * Implements business logic for risk concentration calculations.
 * Follows Flink best practices for windowed processing and keyed streams.
 */
public class ConcentrationCalculationPipeline implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private final String pipelineName;
    private final long windowSizeMillis;
    
    public ConcentrationCalculationPipeline() {
        this("concentration-calculation-pipeline", 60000); // 1 minute window
    }
    
    public ConcentrationCalculationPipeline(String pipelineName, long windowSizeMillis) {
        this.pipelineName = pipelineName;
        this.windowSizeMillis = windowSizeMillis;
    }
    
    /**
     * Processes account data to calculate concentration metrics.
     * Groups accounts by type and calculates risk concentrations.
     * 
     * @param accountStream input stream of Account objects
     * @return DataStream of ConcentrationResult objects
     */
    public DataStream<ConcentrationResult> process(DataStream<Account> accountStream) {
        
        // Key accounts by account type for parallel processing
        KeyedStream<Account, String> keyedAccounts = accountStream
                .keyBy(new AccountTypeKeySelector());
        
        // Apply windowing for time-based aggregations
        DataStream<ConcentrationResult> concentrationResults = keyedAccounts
                .window(TumblingProcessingTimeWindows.of(Time.milliseconds(windowSizeMillis)))
                .aggregate(
                    new ConcentrationAggregator(),
                    new ConcentrationWindowFunction()
                );
        
        // Apply business rules and validations
        return concentrationResults
                .map(new ConcentrationValidationFunction())
                .filter(result -> result != null && result.getStatus() != null);
    }
    
    /**
     * Alternative processing method with custom parallelism.
     * 
     * @param accountStream input stream of Account objects
     * @param parallelism desired parallelism level
     * @return DataStream of ConcentrationResult objects
     */
    public DataStream<ConcentrationResult> process(DataStream<Account> accountStream, int parallelism) {
        return process(accountStream);
    }
    
    /**
     * Key selector for grouping accounts by account type.
     */
    private static class AccountTypeKeySelector implements KeySelector<Account, String> {
        @Override
        public String getKey(Account account) throws Exception {
            return account.getAccountType() != null ? account.getAccountType() : "UNKNOWN";
        }
    }
    
    /**
     * Validation function for concentration results.
     * Applies business rules and data quality checks.
     */
    private static class ConcentrationValidationFunction implements MapFunction<ConcentrationResult, ConcentrationResult> {
        @Override
        public ConcentrationResult map(ConcentrationResult result) throws Exception {
            if (result == null) {
                return null;
            }
            
            // Apply business validation rules
            String status = Constants.STATUS_VALID;
            
            if (result.getConcentrationValue() < 0) {
                status = "INVALID_NEGATIVE_VALUE";
            } else if (result.getConcentrationValue() > 1.0) {
                status = "WARNING_HIGH_CONCENTRATION";
            } else if (result.getTotalExposure() <= 0) {
                status = "INVALID_ZERO_EXPOSURE";
            }
            
            return new ConcentrationResult(
                result.getEntityId(),
                result.getEntityType(),
                result.getConcentrationValue(),
                result.getCalculationMethod(),
                result.getTotalExposure(),
                result.getRiskWeight(),
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
