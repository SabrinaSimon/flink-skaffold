package com.example.flink.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data model representing a product record from source files.
 * This class follows immutable design pattern as recommended for Flink data types.
 */
public class Product {
    
    private final String productId;
    private final String productName;
    private final String category;
    private final double price;
    private final int quantity;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    
    public Product() {
        this(null, null, null, 0.0, 0, null, null, null);
    }
    
    public Product(String productId, String productName, String category, 
                   double price, int quantity, String description, 
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
               quantity == product.quantity &&
               Objects.equals(productId, product.productId) &&
               Objects.equals(productName, product.productName) &&
               Objects.equals(category, product.category) &&
               Objects.equals(description, product.description) &&
               Objects.equals(createdAt, product.createdAt) &&
               Objects.equals(updatedAt, product.updatedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, category, price, quantity, description, createdAt, updatedAt);
    }
    
    @Override
    public String toString() {
        return "Product{" +
               "productId='" + productId + '\'' +
               ", productName='" + productName + '\'' +
               ", category='" + category + '\'' +
               ", price=" + price +
               ", quantity=" + quantity +
               ", description='" + description + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}
