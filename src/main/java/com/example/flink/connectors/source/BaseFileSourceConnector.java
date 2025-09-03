package com.example.flink.connectors.source;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Abstract base class for file-based source connectors.
 * Provides common functionality for reading files in Flink applications.
 * Follows Flink 1.18.1 best practices for file sources.
 */
public abstract class BaseFileSourceConnector {
    
    protected final String sourcePath;
    protected final String connectorName;
    
    protected BaseFileSourceConnector(String sourcePath, String connectorName) {
        this.sourcePath = sourcePath;
        this.connectorName = connectorName;
    }
    
    /**
     * Creates a file source for reading text files line by line.
     * Uses the new FileSource API introduced in Flink 1.14+
     * 
     * @return FileSource configured for text line reading
     */
    protected FileSource<String> createTextFileSource() {
        return FileSource.forRecordStreamFormat(
                new TextLineInputFormat(),
                Path.fromLocalFile(new java.io.File(sourcePath))
        ).build();
    }
    
    /**
     * Creates a DataStream from the file source with proper naming and parallelism.
     * 
     * @param env StreamExecutionEnvironment
     * @return DataStream of raw string records
     */
    public DataStream<String> createSourceStream(StreamExecutionEnvironment env) {
        return env.fromSource(
                createTextFileSource(),
                org.apache.flink.api.common.eventtime.WatermarkStrategy.noWatermarks(),
                connectorName
        ).name(connectorName + "-source")
         .uid(connectorName + "-source-uid");
    }
    
    /**
     * Validates that the source path exists and is readable.
     * 
     * @throws IllegalArgumentException if path is invalid
     */
    protected void validateSourcePath() {
        java.io.File file = new java.io.File(sourcePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Source path does not exist: " + sourcePath);
        }
        if (!file.canRead()) {
            throw new IllegalArgumentException("Source path is not readable: " + sourcePath);
        }
    }
    
    public String getSourcePath() {
        return sourcePath;
    }
    
    public String getConnectorName() {
        return connectorName;
    }
}
