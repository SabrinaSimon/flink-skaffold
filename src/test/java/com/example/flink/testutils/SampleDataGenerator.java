package com.example.flink.testutils;

import com.example.flink.domain.model.Account;
import com.example.flink.domain.model.Product;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Utility class for generating sample data files for testing and development.
 * This class creates realistic test data that follows the expected schema
 * and can be used to validate the entire data processing pipeline.
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public class SampleDataGenerator {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final Random RANDOM = new Random();
    
    // Sample data arrays for realistic test data generation
    private static final String[] ACCOUNT_TYPES = {
        "CHECKING", "SAVINGS", "INVESTMENT", "MONEY_MARKET", "CD", "RETIREMENT"
    };
    
    private static final String[] CURRENCIES = {
        "USD", "EUR", "GBP", "JPY", "CAD", "AUD", "CHF"
    };
    
    private static final String[] PRODUCT_CATEGORIES = {
        "ELECTRONICS", "CLOTHING", "BOOKS", "HOME_GARDEN", "SPORTS", "AUTOMOTIVE", "HEALTH"
    };
    
    private static final String[] ACCOUNT_NAME_PREFIXES = {
        "Personal", "Business", "Corporate", "Family", "Joint", "Trust"
    };
    
    private static final String[] PRODUCT_NAME_ADJECTIVES = {
        "Premium", "Standard", "Deluxe", "Basic", "Professional", "Advanced", "Compact"
    };
    
    private static final String[] PRODUCT_NAME_NOUNS = {
        "Widget", "Device", "Tool", "System", "Component", "Module", "Package"
    };
    
    /**
     * Generates a sample accounts CSV file with the specified number of records.
     * 
     * @param filePath Path where the CSV file should be created
     * @param recordCount Number of account records to generate
     * @throws IOException if file creation fails
     */
    public static void generateAccountsFile(String filePath, int recordCount) throws IOException {
        System.out.println("Generating " + recordCount + " account records to: " + filePath);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write CSV header
            writer.write("accountId,accountName,accountType,balance,currency,createdAt,updatedAt");
            writer.newLine();
            
            for (int i = 1; i <= recordCount; i++) {
                Account account = generateRandomAccount(i);
                writer.write(convertAccountToCsv(account));
                writer.newLine();
                
                if (i % 1000 == 0) {
                    System.out.println("Generated " + i + " account records...");
                }
            }
        }
        
        System.out.println("Successfully generated accounts file: " + filePath);
    }
    
    /**
     * Generates a sample products CSV file with the specified number of records.
     * 
     * @param filePath Path where the CSV file should be created
     * @param recordCount Number of product records to generate
     * @throws IOException if file creation fails
     */
    public static void generateProductsFile(String filePath, int recordCount) throws IOException {
        System.out.println("Generating " + recordCount + " product records to: " + filePath);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write CSV header
            writer.write("productId,productName,category,price,quantity,description,createdAt,updatedAt");
            writer.newLine();
            
            for (int i = 1; i <= recordCount; i++) {
                Product product = generateRandomProduct(i);
                writer.write(convertProductToCsv(product));
                writer.newLine();
                
                if (i % 1000 == 0) {
                    System.out.println("Generated " + i + " product records...");
                }
            }
        }
        
        System.out.println("Successfully generated products file: " + filePath);
    }
    
    /**
     * Generates a random Account object with realistic data.
     * 
     * @param id Sequential ID for the account
     * @return Generated Account object
     */
    private static Account generateRandomAccount(int id) {
        String accountId = "ACC" + String.format("%06d", id);
        String accountType = ACCOUNT_TYPES[RANDOM.nextInt(ACCOUNT_TYPES.length)];
        String accountName = ACCOUNT_NAME_PREFIXES[RANDOM.nextInt(ACCOUNT_NAME_PREFIXES.length)] + 
                           " " + accountType.toLowerCase() + " Account";
        
        // Generate realistic balance based on account type
        double balance = generateRealisticBalance(accountType);
        String currency = CURRENCIES[RANDOM.nextInt(CURRENCIES.length)];
        
        // Generate timestamps (created in the past, updated more recently)
        LocalDateTime createdAt = LocalDateTime.now().minusDays(RANDOM.nextInt(365));
        LocalDateTime updatedAt = createdAt.plusDays(RANDOM.nextInt(30));
        
        return new Account(accountId, accountName, accountType, balance, currency, createdAt, updatedAt);
    }
    
    /**
     * Generates a random Product object with realistic data.
     * 
     * @param id Sequential ID for the product
     * @return Generated Product object
     */
    private static Product generateRandomProduct(int id) {
        String productId = "PROD" + String.format("%06d", id);
        String category = PRODUCT_CATEGORIES[RANDOM.nextInt(PRODUCT_CATEGORIES.length)];
        String productName = PRODUCT_NAME_ADJECTIVES[RANDOM.nextInt(PRODUCT_NAME_ADJECTIVES.length)] + 
                           " " + PRODUCT_NAME_NOUNS[RANDOM.nextInt(PRODUCT_NAME_NOUNS.length)];
        
        // Generate realistic price and quantity
        double price = generateRealisticPrice(category);
        int quantity = RANDOM.nextInt(1000) + 1;
        String description = "High-quality " + productName.toLowerCase() + " for " + category.toLowerCase();
        
        // Generate timestamps
        LocalDateTime createdAt = LocalDateTime.now().minusDays(RANDOM.nextInt(180));
        LocalDateTime updatedAt = createdAt.plusDays(RANDOM.nextInt(15));
        
        return new Product(productId, productName, category, price, quantity, description, createdAt, updatedAt);
    }
    
    /**
     * Generates realistic balance amounts based on account type.
     * 
     * @param accountType Type of the account
     * @return Realistic balance amount
     */
    private static double generateRealisticBalance(String accountType) {
        return switch (accountType) {
            case "CHECKING" -> 1000.0 + (RANDOM.nextDouble() * 9000.0); // $1K - $10K
            case "SAVINGS" -> 5000.0 + (RANDOM.nextDouble() * 45000.0); // $5K - $50K
            case "INVESTMENT" -> 10000.0 + (RANDOM.nextDouble() * 490000.0); // $10K - $500K
            case "MONEY_MARKET" -> 25000.0 + (RANDOM.nextDouble() * 225000.0); // $25K - $250K
            case "CD" -> 10000.0 + (RANDOM.nextDouble() * 90000.0); // $10K - $100K
            case "RETIREMENT" -> 50000.0 + (RANDOM.nextDouble() * 950000.0); // $50K - $1M
            default -> 1000.0 + (RANDOM.nextDouble() * 9000.0);
        };
    }
    
    /**
     * Generates realistic prices based on product category.
     * 
     * @param category Product category
     * @return Realistic price
     */
    private static double generateRealisticPrice(String category) {
        return switch (category) {
            case "ELECTRONICS" -> 50.0 + (RANDOM.nextDouble() * 1950.0); // $50 - $2K
            case "CLOTHING" -> 20.0 + (RANDOM.nextDouble() * 280.0); // $20 - $300
            case "BOOKS" -> 10.0 + (RANDOM.nextDouble() * 90.0); // $10 - $100
            case "HOME_GARDEN" -> 25.0 + (RANDOM.nextDouble() * 475.0); // $25 - $500
            case "SPORTS" -> 30.0 + (RANDOM.nextDouble() * 470.0); // $30 - $500
            case "AUTOMOTIVE" -> 100.0 + (RANDOM.nextDouble() * 4900.0); // $100 - $5K
            case "HEALTH" -> 15.0 + (RANDOM.nextDouble() * 485.0); // $15 - $500
            default -> 10.0 + (RANDOM.nextDouble() * 90.0);
        };
    }
    
    /**
     * Converts an Account object to CSV format.
     * 
     * @param account Account to convert
     * @return CSV string representation
     */
    private static String convertAccountToCsv(Account account) {
        return String.join(",",
            escapeField(account.getAccountId()),
            escapeField(account.getAccountName()),
            escapeField(account.getAccountType()),
            String.valueOf(account.getBalance()),
            escapeField(account.getCurrency()),
            account.getCreatedAt().format(DATE_FORMATTER),
            account.getUpdatedAt().format(DATE_FORMATTER)
        );
    }
    
    /**
     * Converts a Product object to CSV format.
     * 
     * @param product Product to convert
     * @return CSV string representation
     */
    private static String convertProductToCsv(Product product) {
        return String.join(",",
            escapeField(product.getProductId()),
            escapeField(product.getProductName()),
            escapeField(product.getCategory()),
            String.valueOf(product.getPrice()),
            String.valueOf(product.getQuantity()),
            escapeField(product.getDescription()),
            product.getCreatedAt().format(DATE_FORMATTER),
            product.getUpdatedAt().format(DATE_FORMATTER)
        );
    }
    
    /**
     * Escapes CSV field values to handle commas and quotes.
     * 
     * @param field Field value to escape
     * @return Escaped field value
     */
    private static String escapeField(String field) {
        if (field == null) {
            return "";
        }
        
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        
        return field;
    }
    
    /**
     * Main method for generating sample data files.
     * Can be run standalone to create test data.
     * 
     * Usage: java SampleDataGenerator [accounts_count] [products_count]
     */
    public static void main(String[] args) {
        try {
            int accountsCount = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
            int productsCount = args.length > 1 ? Integer.parseInt(args[1]) : 500;
            
            // Create data directory if it doesn't exist
            java.io.File dataDir = new java.io.File("./data/input");
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                if (!created) {
                    System.err.println("Failed to create data directory: " + dataDir.getAbsolutePath());
                    System.exit(1);
                }
            }
            
            // Generate sample files
            generateAccountsFile("./data/input/accounts.csv", accountsCount);
            generateProductsFile("./data/input/products.csv", productsCount);
            
            System.out.println("\nSample data generation completed successfully!");
            System.out.println("Files created:");
            System.out.println("- ./data/input/accounts.csv (" + accountsCount + " records)");
            System.out.println("- ./data/input/products.csv (" + productsCount + " records)");
            
        } catch (Exception e) {
            System.err.println("Error generating sample data: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
