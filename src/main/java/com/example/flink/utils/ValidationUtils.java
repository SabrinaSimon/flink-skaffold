package com.example.flink.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for common validation operations across the application.
 * Provides centralized validation logic that can be reused in multiple components.
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public final class ValidationUtils {
    
    // Prevent instantiation of utility class
    private ValidationUtils() {
        throw new UnsupportedOperationException("ValidationUtils class cannot be instantiated");
    }
    
    /**
     * Validates if a string is not null and not empty after trimming.
     * 
     * @param value The string value to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validates if a string is not null, not empty, and has minimum length.
     * 
     * @param value The string value to validate
     * @param minLength Minimum required length
     * @return true if valid, false otherwise
     */
    public static boolean isValidString(String value, int minLength) {
        return isValidString(value) && value.trim().length() >= minLength;
    }
    
    /**
     * Validates if a numeric value is within specified range (inclusive).
     * 
     * @param value The value to validate
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return true if valid, false otherwise
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }
    
    /**
     * Validates if a numeric value is positive (greater than zero).
     * 
     * @param value The value to validate
     * @return true if positive, false otherwise
     */
    public static boolean isPositive(double value) {
        return value > 0;
    }
    
    /**
     * Validates if a numeric value is non-negative (greater than or equal to zero).
     * 
     * @param value The value to validate
     * @return true if non-negative, false otherwise
     */
    public static boolean isNonNegative(double value) {
        return value >= 0;
    }
    
    /**
     * Validates if an integer value is within specified range (inclusive).
     * 
     * @param value The value to validate
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return true if valid, false otherwise
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Validates if a date is not null and not in the future.
     * 
     * @param date The date to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPastDate(LocalDateTime date) {
        return date != null && date.isBefore(LocalDateTime.now());
    }
    
    /**
     * Validates if a date is not null and within a reasonable range.
     * 
     * @param date The date to validate
     * @param yearsBack Maximum years in the past allowed
     * @param yearsFuture Maximum years in the future allowed
     * @return true if valid, false otherwise
     */
    public static boolean isValidDateRange(LocalDateTime date, int yearsBack, int yearsFuture) {
        if (date == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDate = now.minusYears(yearsBack);
        LocalDateTime maxDate = now.plusYears(yearsFuture);
        
        return date.isAfter(minDate) && date.isBefore(maxDate);
    }
    
    /**
     * Validates if a currency code is in valid format (3 uppercase letters).
     * 
     * @param currencyCode The currency code to validate
     * @return true if valid format, false otherwise
     */
    public static boolean isValidCurrencyCode(String currencyCode) {
        return isValidString(currencyCode) && 
               currencyCode.length() == 3 && 
               currencyCode.matches("[A-Z]{3}");
    }
    
    /**
     * Validates if an account ID follows the expected format (prefix + digits).
     * 
     * @param accountId The account ID to validate
     * @param prefix Expected prefix (e.g., "ACC")
     * @param digitCount Expected number of digits after prefix
     * @return true if valid format, false otherwise
     */
    public static boolean isValidAccountId(String accountId, String prefix, int digitCount) {
        if (!isValidString(accountId) || !isValidString(prefix)) {
            return false;
        }
        
        String pattern = prefix + "\\d{" + digitCount + "}";
        return accountId.matches(pattern);
    }
    
    /**
     * Validates if a product ID follows the expected format (prefix + digits).
     * 
     * @param productId The product ID to validate
     * @param prefix Expected prefix (e.g., "PROD")
     * @param digitCount Expected number of digits after prefix
     * @return true if valid format, false otherwise
     */
    public static boolean isValidProductId(String productId, String prefix, int digitCount) {
        return isValidAccountId(productId, prefix, digitCount); // Same validation logic
    }
    
    /**
     * Validates if a quality score is within valid range (0.0 to 1.0).
     * 
     * @param qualityScore The quality score to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidQualityScore(Double qualityScore) {
        return qualityScore != null && isInRange(qualityScore, Constants.MIN_QUALITY_SCORE, Constants.MAX_QUALITY_SCORE);
    }
    
    /**
     * Validates if a confidence level is within valid range (0.0 to 1.0).
     * 
     * @param confidenceLevel The confidence level to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidConfidenceLevel(Double confidenceLevel) {
        return confidenceLevel != null && isInRange(confidenceLevel, 0.0, 1.0);
    }
    
    /**
     * Validates if an account type is one of the supported types.
     * 
     * @param accountType The account type to validate
     * @return true if supported type, false otherwise
     */
    public static boolean isValidAccountType(String accountType) {
        if (!isValidString(accountType)) {
            return false;
        }
        
        return accountType.equals(Constants.ACCOUNT_TYPE_CHECKING) ||
               accountType.equals(Constants.ACCOUNT_TYPE_SAVINGS) ||
               accountType.equals(Constants.ACCOUNT_TYPE_INVESTMENT) ||
               accountType.equals(Constants.ACCOUNT_TYPE_MONEY_MARKET) ||
               accountType.equals(Constants.ACCOUNT_TYPE_CD) ||
               accountType.equals(Constants.ACCOUNT_TYPE_RETIREMENT);
    }
    
    /**
     * Validates if a status value is one of the supported statuses.
     * 
     * @param status The status to validate
     * @return true if supported status, false otherwise
     */
    public static boolean isValidStatus(String status) {
        if (!isValidString(status)) {
            return false;
        }
        
        return status.equals(Constants.STATUS_VALID) ||
               status.equals(Constants.STATUS_INVALID) ||
               status.equals(Constants.STATUS_ERROR) ||
               status.equals(Constants.STATUS_WARNING) ||
               status.equals(Constants.STATUS_CALCULATED) ||
               status.equals(Constants.STATUS_PENDING);
    }
    
    /**
     * Validates if a parallelism value is reasonable for processing.
     * 
     * @param parallelism The parallelism value to validate
     * @return true if reasonable value, false otherwise
     */
    public static boolean isValidParallelism(int parallelism) {
        return isInRange(parallelism, 1, 1000); // Reasonable range: 1 to 1000
    }
    
    /**
     * Validates if a window size in milliseconds is reasonable.
     * 
     * @param windowSizeMs The window size in milliseconds
     * @return true if reasonable value, false otherwise
     */
    public static boolean isValidWindowSize(long windowSizeMs) {
        // Reasonable range: 1 second to 1 day
        return windowSizeMs >= 1000L && windowSizeMs <= 86_400_000L;
    }
    
    /**
     * Sanitizes a string for CSV output by escaping special characters.
     * 
     * @param value The string value to sanitize
     * @return Sanitized string safe for CSV output
     */
    public static String sanitizeForCsv(String value) {
        if (value == null) {
            return "";
        }
        
        // Escape quotes and wrap in quotes if contains comma, quote, or newline
        if (value.contains(Constants.CSV_DELIMITER) || 
            value.contains(Constants.CSV_QUOTE) || 
            value.contains("\n") || 
            value.contains("\r")) {
            return Constants.CSV_QUOTE + value.replace(Constants.CSV_QUOTE, Constants.CSV_QUOTE + Constants.CSV_QUOTE) + Constants.CSV_QUOTE;
        }
        
        return value;
    }
    
    /**
     * Validates and parses a date string in ISO format.
     * 
     * @param dateString The date string to parse
     * @return Parsed LocalDateTime or null if invalid
     */
    public static LocalDateTime parseDate(String dateString) {
        if (!isValidString(dateString)) {
            return null;
        }
        
        try {
            return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Validates and parses a double value from string.
     * 
     * @param valueString The string representation of the double
     * @return Parsed double value or null if invalid
     */
    public static Double parseDouble(String valueString) {
        if (!isValidString(valueString)) {
            return null;
        }
        
        try {
            return Double.parseDouble(valueString.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Validates and parses an integer value from string.
     * 
     * @param valueString The string representation of the integer
     * @return Parsed integer value or null if invalid
     */
    public static Integer parseInt(String valueString) {
        if (!isValidString(valueString)) {
            return null;
        }
        
        try {
            return Integer.parseInt(valueString.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
