package com.example.flink.connectors.source;

import com.example.flink.domain.model.Account;
import com.example.flink.connectors.serialization.AccountDeserializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * File source connector specifically for Account data files.
 * Extends the base file connector with Account-specific parsing logic.
 * Follows Flink best practices for type-safe data processing.
 */
public class AccountFileSourceConnector extends BaseFileSourceConnector {
    
    private static final String DEFAULT_CONNECTOR_NAME = "account-file-source";
    
    public AccountFileSourceConnector(String sourcePath) {
        super(sourcePath, DEFAULT_CONNECTOR_NAME);
        validateSourcePath();
    }
    
    public AccountFileSourceConnector(String sourcePath, String connectorName) {
        super(sourcePath, connectorName);
        validateSourcePath();
    }
    
    /**
     * Creates a typed DataStream of Account objects from CSV files.
     * Uses custom deserialization logic for Account records.
     * 
     * @param env StreamExecutionEnvironment
     * @return DataStream of Account objects
     */
    public DataStream<Account> createAccountStream(StreamExecutionEnvironment env) {
        return createSourceStream(env)
                .map(new AccountDeserializer())
                .filter(account -> account != null && account.getAccountId() != null)
                .name("account-deserializer")
                .uid("account-deserializer-uid");
    }
    
    /**
     * Creates a DataStream with custom parallelism for high-throughput scenarios.
     * 
     * @param env StreamExecutionEnvironment
     * @param parallelism desired parallelism level
     * @return DataStream of Account objects
     */
    public DataStream<Account> createAccountStream(StreamExecutionEnvironment env, int parallelism) {
        return createAccountStream(env)
                .setParallelism(parallelism);
    }
}
