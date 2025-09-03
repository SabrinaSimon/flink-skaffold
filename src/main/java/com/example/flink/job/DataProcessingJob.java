package com.example.flink.job;

import com.example.flink.config.FlinkConfigurationProperties;
import com.example.flink.connectors.source.AccountFileSourceConnector;
import com.example.flink.connectors.source.ProductFileSourceConnector;
import com.example.flink.domain.model.Account;
import com.example.flink.domain.model.CashResult;
import com.example.flink.domain.model.ConcentrationResult;
import com.example.flink.domain.model.Product;
import com.example.flink.processing.pipeline.CashProcessingPipeline;
import com.example.flink.processing.pipeline.ConcentrationCalculationPipeline;
import com.example.flink.connectors.sink.CashResultFileSink;
import com.example.flink.connectors.sink.ConcentrationResultFileSink;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Main Flink job class for the data processing application.
 * This is a Spring-managed component that orchestrates the entire data processing pipeline.
 * 
 * Architecture:
 * 1. File Sources (Account, Product) -> 
 * 2. Processing Pipelines (Concentration, Cash) -> 
 * 3. File Sinks (Results)
 */
@Component
public class DataProcessingJob {
    
    private static final Logger logger = LoggerFactory.getLogger(DataProcessingJob.class);
    private final FlinkConfigurationProperties config;
    
    public DataProcessingJob(FlinkConfigurationProperties config) {
        if (config == null) {
            throw new IllegalArgumentException("FlinkConfigurationProperties cannot be null");
        }
        this.config = config;
    }
    
    /**
     * Executes the Flink data processing job.
     * This method is called by the Spring Boot application.
     */
    public void execute() throws Exception {
        // Setup Flink execution environment
        StreamExecutionEnvironment env = createExecutionEnvironment();
        
        // Build and execute the data processing pipeline
        buildDataProcessingPipeline(env);
        
        // Execute the job
        env.execute("Flink Data Processing Job - " + config.getApplication().getName());
    }
    
    /**
     * Creates and configures the Flink execution environment.
     * Applies configuration from Spring Boot properties.
     */
    private StreamExecutionEnvironment createExecutionEnvironment() {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        // Set execution mode (streaming by default)
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        
        // Set parallelism from configuration
        env.setParallelism(config.getProcessing().getParallelism());
        
        // Enable checkpointing for fault tolerance
        env.enableCheckpointing(config.getProcessing().getCheckpointing().getInterval());
        
        return env;
    }
    
    /**
     * Builds the complete data processing pipeline.
     * Connects sources, processing pipelines, and sinks using Spring configuration.
     */
    private void buildDataProcessingPipeline(StreamExecutionEnvironment env) {
        
        // === SOURCE LAYER ===
        // Create file source connectors with Spring configuration
        AccountFileSourceConnector accountSource = new AccountFileSourceConnector(config.getSource().getAccount().getPath());
        ProductFileSourceConnector productSource = new ProductFileSourceConnector(config.getSource().getProduct().getPath());
        
        // Create data streams from sources
        DataStream<Account> accountStream = accountSource.createAccountStream(env);
        
        DataStream<Product> productStream = productSource.createProductStream(env);
        
        // === PROCESSING LAYER ===
        // Initialize processing pipelines
        ConcentrationCalculationPipeline concentrationPipeline = new ConcentrationCalculationPipeline();
        CashProcessingPipeline cashPipeline = new CashProcessingPipeline();
        
        // Process account data through concentration calculation pipeline
        DataStream<ConcentrationResult> concentrationResults = concentrationPipeline
                .process(accountStream, config.getProcessing().getParallelism());
        
        // Process account data through cash processing pipeline
        DataStream<CashResult> cashResults = cashPipeline
                .process(accountStream, config.getProcessing().getParallelism());
        
        // === SINK LAYER ===
        // Create file sink connectors with Spring configuration
        ConcentrationResultFileSink concentrationSink = new ConcentrationResultFileSink(config.getSink().getConcentration().getPath());
        CashResultFileSink cashSink = new CashResultFileSink(config.getSink().getCash().getPath());
        
        // Connect processed streams to sinks
        concentrationSink.addSink(concentrationResults, config.getSink().getParallelism());
        cashSink.addSink(cashResults, config.getSink().getParallelism());
        
        // === MONITORING & LOGGING ===
        // Add monitoring outputs if enabled
        if (config.getMetrics().isEnabled()) {
            addMonitoringOutputs(concentrationResults, cashResults);
        }
        
        logger.info("Data processing pipeline configured successfully:");
        logger.info("- Application: {} v{}", config.getApplication().getName(), config.getApplication().getVersion());
        logger.info("- Account Source: {}", config.getSource().getAccount().getPath());
        logger.info("- Product Source: {}", config.getSource().getProduct().getPath());
        logger.info("- Concentration Output: {}", config.getSink().getConcentration().getPath());
        logger.info("- Cash Output: {}", config.getSink().getCash().getPath());
        logger.info("- Processing Parallelism: {}", config.getProcessing().getParallelism());
        logger.info("- Validation Enabled: {}", config.getQuality().getValidation().isEnabled());
    }
    
    /**
     * Adds monitoring outputs for operational visibility.
     * Logs processing statistics and metrics.
     */
    private void addMonitoringOutputs(DataStream<ConcentrationResult> concentrationResults, 
                                    DataStream<CashResult> cashResults) {
        
        // Add logging for concentration results
        concentrationResults
                .map(result -> {
                    logger.debug("Processed concentration for: {}, Value: {}, Status: {}", 
                               result.getEntityId(), result.getConcentrationValue(), result.getStatus());
                    return result;
                })
                .name("concentration-logging")
                .uid("concentration-logging-uid");
        
        // Add logging for cash results
        cashResults
                .map(result -> {
                    logger.debug("Processed cash for account: {}, Position: {}, Status: {}", 
                               result.getAccountId(), result.getCashPosition(), result.getStatus());
                    return result;
                })
                .name("cash-logging")
                .uid("cash-logging-uid");
    }
}
