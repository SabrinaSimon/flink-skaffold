package com.example.flink.connectors.source;

import com.example.flink.domain.model.Account;
import com.example.flink.connectors.serialization.AccountDeserializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * File source connector for Account data streams.
 * Handles reading Account data from file sources with proper error handling and serialization.
 */
public class AccountFileSourceConnector {
    
    private final String filePath;
    
    public AccountFileSourceConnector(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Creates a DataStream of Account objects from file source.
     * 
     * @param env StreamExecutionEnvironment
     * @return DataStream of Account objects
     */
    public DataStream<Account> createAccountStream(StreamExecutionEnvironment env) {
        return env
            .readTextFile(filePath)
            .map(new AccountDeserializer())
            .filter(account -> account != null);
    }
    
    /**
     * Creates a DataStream with custom parallelism.
     * 
     * @param env StreamExecutionEnvironment
     * @param parallelism desired parallelism level
     * @return DataStream of Account objects
     */
    public DataStream<Account> createAccountStream(StreamExecutionEnvironment env, int parallelism) {
        return createAccountStream(env);
    }
}
