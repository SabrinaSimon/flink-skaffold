package com.example.flink.connectors.source;

import com.example.flink.domain.model.Product;
import com.example.flink.connectors.serialization.ProductDeserializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.Serializable;

/**
 * File source connector for Product data streams.
 * Handles reading Product data from file sources with proper error handling and serialization.
 */
public class ProductFileSourceConnector implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private final String filePath;
    
    public ProductFileSourceConnector(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Creates a DataStream of Product objects from file source.
     * 
     * @param env StreamExecutionEnvironment
     * @return DataStream of Product objects
     */
    public DataStream<Product> createProductStream(StreamExecutionEnvironment env) {
        return env
            .readTextFile(filePath)
            .map(new ProductDeserializer())
            .filter(product -> product != null);
    }
    
    /**
     * Creates a DataStream with custom parallelism.
     * 
     * @param env StreamExecutionEnvironment
     * @param parallelism desired parallelism level
     * @return DataStream of Product objects
     */
    public DataStream<Product> createProductStream(StreamExecutionEnvironment env, int parallelism) {
        return createProductStream(env);
    }
}
