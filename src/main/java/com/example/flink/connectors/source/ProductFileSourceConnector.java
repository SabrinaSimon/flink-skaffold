package com.example.flink.connectors.source;

import com.example.flink.domain.model.Product;
import com.example.flink.connectors.serialization.ProductDeserializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * File source connector specifically for Product data files.
 * Extends the base file connector with Product-specific parsing logic.
 * Follows Flink best practices for type-safe data processing.
 */
public class ProductFileSourceConnector extends BaseFileSourceConnector {
    
    private static final String DEFAULT_CONNECTOR_NAME = "product-file-source";
    
    public ProductFileSourceConnector(String sourcePath) {
        super(sourcePath, DEFAULT_CONNECTOR_NAME);
        validateSourcePath();
    }
    
    public ProductFileSourceConnector(String sourcePath, String connectorName) {
        super(sourcePath, connectorName);
        validateSourcePath();
    }
    
    /**
     * Creates a typed DataStream of Product objects from CSV files.
     * Uses custom deserialization logic for Product records.
     * 
     * @param env StreamExecutionEnvironment
     * @return DataStream of Product objects
     */
    public DataStream<Product> createProductStream(StreamExecutionEnvironment env) {
        return createSourceStream(env)
                .map(new ProductDeserializer())
                .filter(product -> product != null && product.getProductId() != null)
                .name("product-deserializer")
                .uid("product-deserializer-uid");
    }
    
    /**
     * Creates a DataStream with custom parallelism for high-throughput scenarios.
     * 
     * @param env StreamExecutionEnvironment
     * @param parallelism desired parallelism level
     * @return DataStream of Product objects
     */
    public DataStream<Product> createProductStream(StreamExecutionEnvironment env, int parallelism) {
        return createProductStream(env)
                .setParallelism(parallelism);
    }
}
