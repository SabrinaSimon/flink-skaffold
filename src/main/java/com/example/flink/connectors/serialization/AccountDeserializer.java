package com.example.flink.connectors.serialization;

import com.example.flink.domain.model.Account;
import com.example.flink.utils.Constants;
import com.example.flink.utils.ValidationUtils;
import org.apache.flink.api.common.functions.MapFunction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deserializer for converting CSV strings to Account objects.
 * Implements MapFunction for use in Flink transformations.
 * Handles data validation and error recovery.
 */
public class AccountDeserializer implements MapFunction<String, Account> {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String CSV_DELIMITER = Constants.CSV_DELIMITER;
    
    @Override
    public Account map(String csvLine) throws Exception {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }
        
        try {
            String[] fields = csvLine.split(CSV_DELIMITER, -1);
            
            // Expected format: accountId,accountName,accountType,balance,currency,createdAt,updatedAt
            if (fields.length < 7) {
                throw new IllegalArgumentException("Invalid CSV format, expected 7 fields, got " + fields.length);
            }
            
            String accountId = parseStringField(fields[0]);
            String accountName = parseStringField(fields[1]);
            String accountType = parseStringField(fields[2]);
            double balance = parseDoubleField(fields[3]);
            String currency = parseStringField(fields[4]);
            LocalDateTime createdAt = parseDateTimeField(fields[5]);
            LocalDateTime updatedAt = parseDateTimeField(fields[6]);
            
            return new Account(accountId, accountName, accountType, balance, currency, createdAt, updatedAt);
            
        } catch (Exception e) {
            // Log the error and return null to filter out invalid records
            System.err.println("Error parsing Account CSV line: " + csvLine + ", Error: " + e.getMessage());
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
