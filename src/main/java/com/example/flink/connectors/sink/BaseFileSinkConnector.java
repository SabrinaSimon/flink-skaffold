package com.example.flink.connectors.sink;

import org.apache.flink.api.common.serialization.Encoder;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;

/**
 * Abstract base class for file-based sink connectors.
 * Provides common functionality for writing data to files in Flink applications.
 * Follows Flink 1.18.1 best practices for file sinks with rolling policies.
 */
public abstract class BaseFileSinkConnector<T> {
    
    protected final String outputPath;
    protected final String sinkName;
    
    protected BaseFileSinkConnector(String outputPath, String sinkName) {
        this.outputPath = outputPath;
        this.sinkName = sinkName;
    }
    
    /**
     * Creates a FileSink with default rolling policy and CSV output format.
     * Implements best practices for file writing in production environments.
     * 
     * @return Configured FileSink
     */
    protected FileSink<T> createFileSink() {
        return FileSink.<T>forRowFormat(
                new Path(outputPath),
                createEncoder()
        )
        .withRollingPolicy(
            DefaultRollingPolicy.builder()
                .withRolloverInterval(Duration.ofMinutes(5))  // Roll every 5 minutes
                .withInactivityInterval(Duration.ofMinutes(1)) // Roll after 1 minute of inactivity
                .withMaxPartSize(1024 * 1024 * 128)  // Roll when file reaches 128MB
                .build()
        )
        .withBucketAssigner(new DateTimeBucketAssigner<T>())
        .build();
    }
    
    /**
     * Creates an encoder for converting objects to string format.
     * Must be implemented by concrete sink classes.
     * 
     * @return Encoder for the specific data type
     */
    protected abstract Encoder<T> createEncoder();
    
    /**
     * Validates the output path and creates directories if necessary.
     * 
     * @throws IllegalArgumentException if path is invalid
     */
    protected void validateAndCreateOutputPath() {
        java.io.File outputDir = new java.io.File(outputPath);
        java.io.File parentDir = outputDir.getParentFile();
        
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IllegalArgumentException("Failed to create output directory: " + parentDir.getAbsolutePath());
            }
        }
        
        if (outputDir.exists() && !outputDir.canWrite()) {
            throw new IllegalArgumentException("Output path is not writable: " + outputPath);
        }
    }
    
    /**
     * Base encoder implementation that converts objects to CSV format.
     */
    protected abstract class CsvEncoder implements Encoder<T> {
        
        @Override
        public void encode(T element, OutputStream stream) throws IOException {
            if (element != null) {
                String csvLine = convertToCsv(element) + "\n";
                stream.write(csvLine.getBytes());
            }
        }
        
        /**
         * Converts the object to CSV format.
         * Must be implemented by concrete encoders.
         * 
         * @param element the object to convert
         * @return CSV string representation
         */
        protected abstract String convertToCsv(T element);
    }
    
    public String getOutputPath() {
        return outputPath;
    }
    
    public String getSinkName() {
        return sinkName;
    }
}
