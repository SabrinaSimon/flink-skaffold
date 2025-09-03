package com.example.flink.interfaces;

import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * Generic interface for data sink connectors in the Flink data processing pipeline.
 * This interface provides a contract for implementing various types of data sinks
 * while maintaining consistency across different sink implementations.
 * 
 * @param <T> The type of data objects that this sink will consume
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public interface DataSinkConnector<T> {
    
    /**
     * Adds this sink to the provided DataStream.
     * This method should handle all sink-specific logic including:
     * - Data serialization and formatting
     * - Connection management
     * - Error handling and retry logic
     * - Batching and flushing strategies
     * 
     * @param dataStream The DataStream to be consumed by this sink
     * @throws Exception if sink creation or attachment fails
     */
    void addSink(DataStream<T> dataStream) throws Exception;
    
    /**
     * Adds this sink with custom parallelism settings.
     * Useful for optimizing performance based on sink characteristics.
     * 
     * @param dataStream The DataStream to be consumed by this sink
     * @param parallelism The desired parallelism level for this sink
     * @throws Exception if sink creation or attachment fails
     */
    void addSink(DataStream<T> dataStream, int parallelism) throws Exception;
    
    /**
     * Validates the sink configuration and connectivity.
     * Should be called before attaching the sink to a DataStream.
     * 
     * @return true if the sink is valid and accessible, false otherwise
     */
    boolean validateSink();
    
    /**
     * Returns the name identifier for this sink connector.
     * Used for monitoring, logging, and debugging purposes.
     * 
     * @return A unique identifier for this sink connector
     */
    String getSinkName();
    
    /**
     * Returns sink-specific configuration properties.
     * Useful for runtime monitoring and troubleshooting.
     * 
     * @return A description of the sink configuration
     */
    String getSinkDescription();
    
    /**
     * Flushes any buffered data to the sink destination.
     * Should be called during graceful shutdown or checkpointing.
     * 
     * @throws Exception if flush operation fails
     */
    void flush() throws Exception;
    
    /**
     * Cleanup method called when the sink is no longer needed.
     * Should release any resources held by the connector.
     */
    void cleanup();
}
