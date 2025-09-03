package com.example.flink.interfaces;

/**
 * Generic interface for data serialization and deserialization operations.
 * This interface provides a contract for converting between different data formats
 * commonly used in data processing pipelines.
 * 
 * @param <T> The data type to be serialized/deserialized
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public interface DataSerializer<T> {
    
    /**
     * Serializes an object to its string representation.
     * Commonly used formats include CSV, JSON, XML, Avro, etc.
     * 
     * @param data The object to be serialized
     * @return String representation of the object
     * @throws SerializationException if serialization fails
     */
    String serialize(T data) throws SerializationException;
    
    /**
     * Deserializes a string representation back to an object.
     * Should handle malformed data gracefully.
     * 
     * @param serializedData The string representation to deserialize
     * @return The deserialized object
     * @throws SerializationException if deserialization fails
     */
    T deserialize(String serializedData) throws SerializationException;
    
    /**
     * Validates if the provided string can be deserialized to the expected type.
     * Useful for data quality checks before processing.
     * 
     * @param serializedData The string to validate
     * @return true if data can be successfully deserialized, false otherwise
     */
    boolean isValidFormat(String serializedData);
    
    /**
     * Returns the format type supported by this serializer.
     * Examples: "CSV", "JSON", "AVRO", "PARQUET"
     * 
     * @return The format identifier
     */
    String getFormatType();
    
    /**
     * Returns configuration details about the serialization format.
     * For CSV: delimiter, quote character, escape character
     * For JSON: schema, date formats
     * 
     * @return Configuration details as a formatted string
     */
    String getFormatConfiguration();
    
    /**
     * Custom exception class for serialization errors.
     */
    class SerializationException extends Exception {
        public SerializationException(String message) {
            super(message);
        }
        
        public SerializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
