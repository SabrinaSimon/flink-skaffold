package com.example.flink.connectors.source;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Generic interface for all source connectors in the Flink data processing pipeline.
 * This interface provides a contract for implementing various types of data sources
 * while maintaining consistency across different source implementations.
 * 
 * @param <T> The type of data objects that this source will produce
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public interface DataSourceConnector<T> {
    
    /**
     * Creates and returns a DataStream from the configured source.
     * This method should handle all source-specific logic including:
     * - Connection establishment
     * - Data format parsing
     * - Error handling and recovery
     * - Watermark generation (if applicable)
     * 
     * @param env The Flink StreamExecutionEnvironment
     * @return A configured DataStream of type T
     * @throws Exception if source creation fails
     */
    DataStream<T> createSourceStream(StreamExecutionEnvironment env) throws Exception;
    
    /**
     * Creates a DataStream with custom parallelism settings.
     * Useful for optimizing performance based on source characteristics.
     * 
     * @param env The Flink StreamExecutionEnvironment
     * @param parallelism The desired parallelism level for this source
     * @return A configured DataStream of type T
     * @throws Exception if source creation fails
     */
    DataStream<T> createSourceStream(StreamExecutionEnvironment env, int parallelism) throws Exception;
    
    /**
     * Validates the source configuration and connectivity.
     * Should be called before creating the actual source stream.
     * 
     * @return true if the source is valid and accessible, false otherwise
     */
    boolean validateSource();
    
    /**
     * Returns the name identifier for this source connector.
     * Used for monitoring, logging, and debugging purposes.
     * 
     * @return A unique identifier for this source connector
     */
    String getSourceName();
    
    /**
     * Returns source-specific configuration properties.
     * Useful for runtime monitoring and troubleshooting.
     * 
     * @return A description of the source configuration
     */
    String getSourceDescription();
    
    /**
     * Cleanup method called when the source is no longer needed.
     * Should release any resources held by the connector.
     */
    void cleanup();
}
