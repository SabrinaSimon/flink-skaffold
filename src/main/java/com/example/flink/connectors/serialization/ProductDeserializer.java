package com.example.flink.connectors.serialization;

import com.example.flink.domain.model.Product;
import com.example.flink.utils.Constants;
import com.example.flink.utils.ValidationUtils;
import org.apache.flink.api.common.functions.MapFunction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deserializer for converting CSV strings to Product objects.
 * Implements MapFunction for use in Flink transformations.
 * Handles data validation and error recovery.
 */
public class ProductDeserializer implements MapFunction<String, Product> {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public Product map(String csvLine) throws Exception {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        
        try {
            String[] fields = csvLine.split(Constants.CSV_DELIMITER, -1);
            
            // Expected format: productId,productName,category,price,quantity,description,createdAt,updatedAt
            if (fields.length < 8) {
                throw new IllegalArgumentException("Invalid CSV format, expected 8 fields, got " + fields.length);
            }
            
            String productId = parseStringField(fields[0]);
            String productName = parseStringField(fields[1]);
            String category = parseStringField(fields[2]);
            double price = parseDoubleField(fields[3]);
            int quantity = parseIntField(fields[4]);
            String description = parseStringField(fields[5]);
            LocalDateTime createdAt = parseDateTimeField(fields[6]);
            LocalDateTime updatedAt = parseDateTimeField(fields[7]);
            
            return new Product(productId, productName, category, price, quantity, description, createdAt, updatedAt);
            
        } catch (Exception e) {
            // Log the error and return null to filter out invalid records
            System.err.println("Error parsing Product CSV line: " + csvLine + ", Error: " + e.getMessage());
            return null;
        }
    }
    
    private String parseStringField(String field) {
        return field != null && !field.trim().isEmpty() ? field.trim() : null;
    }
    
    private double parseDoubleField(String field) {
        try {
            return field != null && !field.trim().isEmpty() ? Double.parseDouble(field.trim()) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private int parseIntField(String field) {
        try {
            return field != null && !field.trim().isEmpty() ? Integer.parseInt(field.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private LocalDateTime parseDateTimeField(String field) {
        try {
            return field != null && !field.trim().isEmpty() ? 
                   LocalDateTime.parse(field.trim(), DATE_FORMATTER) : 
                   LocalDateTime.now();
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
