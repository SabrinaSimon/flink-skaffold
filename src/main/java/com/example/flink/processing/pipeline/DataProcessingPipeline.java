package com.example.flink.interfaces;

import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * Generic interface for data processing pipelines in the Flink application.
 * This interface defines the contract for implementing business logic transformations
 * that can be composed together to build complex data processing workflows.
 * 
 * @param <INPUT> The input data type for this pipeline
 * @param <OUTPUT> The output data type after processing
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public interface DataProcessingPipeline<INPUT, OUTPUT> {
    
    /**
     * Main processing method that transforms input data stream to output data stream.
     * This method should contain all the business logic for data transformation including:
     * - Data validation and cleansing
     * - Business rule application
     * - Aggregations and calculations
     * - Data enrichment
     * - Quality checks
     * 
     * @param inputStream The input DataStream to be processed
     * @return The processed DataStream of output type
     * @throws Exception if processing fails
     */
    DataStream<OUTPUT> process(DataStream<INPUT> inputStream) throws Exception;
    
    /**
     * Processing method with custom parallelism for performance tuning.
     * Allows fine-grained control over resource allocation for specific pipelines.
     * 
     * @param inputStream The input DataStream to be processed
     * @param parallelism The desired parallelism level for this pipeline
     * @return The processed DataStream of output type
     * @throws Exception if processing fails
     */
    DataStream<OUTPUT> process(DataStream<INPUT> inputStream, int parallelism) throws Exception;
    
    /**
     * Validates the pipeline configuration and business rules.
     * Should be called before starting the actual processing.
     * 
     * @return true if the pipeline is properly configured, false otherwise
     */
    boolean validateConfiguration();
    
    /**
     * Returns the pipeline name for identification and monitoring.
     * 
     * @return A unique identifier for this processing pipeline
     */
    String getPipelineName();
    
    /**
     * Returns a description of what this pipeline does.
     * Useful for documentation and operational understanding.
     * 
     * @return A human-readable description of the pipeline's purpose
     */
    String getPipelineDescription();
    
    /**
     * Returns processing statistics and metrics.
     * Can be used for monitoring and performance analysis.
     * 
     * @return Pipeline metrics as a formatted string
     */
    String getProcessingMetrics();
    
    /**
     * Resets any internal state maintained by the pipeline.
     * Useful for testing and recovery scenarios.
     */
    void resetState();
}
