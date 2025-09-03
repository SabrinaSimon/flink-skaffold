package com.example.flink.model.base;

import com.example.flink.utils.Constants;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base class for all calculation result models in the Flink processing pipeline.
 * Extends BaseDataModel with additional fields specific to calculation outputs.
 * This ensures consistency across all types of calculation results.
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseCalculationResult extends BaseDataModel {
    
    /**
     * Identifier of the entity this calculation was performed on.
     * Links the result back to the source entity.
     */
    protected final String entityId;
    
    /**
     * Type of the entity this calculation was performed on.
     * Examples: "ACCOUNT", "PRODUCT", "TRANSACTION", "PORTFOLIO"
     */
    protected final String entityType;
    
    /**
     * Method or algorithm used for this calculation.
     * Examples: "STANDARD_CALCULATION", "WEIGHTED_AVERAGE", "MONTE_CARLO"
     */
    protected final String calculationMethod;
    
    /**
     * Timestamp when the calculation was performed.
     * Different from processingTimestamp which is when data entered the pipeline.
     */
    protected final LocalDateTime calculatedAt;
    
    /**
     * Status of the calculation result.
     * Examples: "VALID", "WARNING", "ERROR", "PENDING_REVIEW"
     */
    protected final String status;
    
    /**
     * Confidence level in the calculation result (0.0 to 1.0).
     * Higher values indicate more reliable results.
     */
    protected final Double confidenceLevel;
    
    /**
     * Additional metadata or context about the calculation.
     * Can store JSON, key-value pairs, or other structured data.
     */
    protected final String metadata;
    
    /**
     * Number of input records used in this calculation.
     * Useful for understanding the basis of the calculation.
     */
    protected final Integer inputRecordCount;
    
    /**
     * Constructor for creating a new BaseCalculationResult instance.
     * 
     * @param id Unique identifier for this result
     * @param createdAt Timestamp when record was created
     * @param updatedAt Timestamp when record was last updated
     * @param version Version number for concurrency control
     * @param sourceSystem Source system identifier
     * @param processingTimestamp Processing timestamp
     * @param qualityScore Data quality score (0.0 to 1.0)
     * @param entityId Identifier of the calculated entity
     * @param entityType Type of the calculated entity
     * @param calculationMethod Method used for calculation
     * @param calculatedAt When calculation was performed
     * @param status Status of the calculation
     * @param confidenceLevel Confidence in the result (0.0 to 1.0)
     * @param metadata Additional calculation metadata
     * @param inputRecordCount Number of input records used
     */
    protected BaseCalculationResult(String id, LocalDateTime createdAt, LocalDateTime updatedAt,
                                   Long version, String sourceSystem, LocalDateTime processingTimestamp,
                                   Double qualityScore, String entityId, String entityType,
                                   String calculationMethod, LocalDateTime calculatedAt, String status,
                                   Double confidenceLevel, String metadata, Integer inputRecordCount) {
        super(id, createdAt, updatedAt, version, sourceSystem, processingTimestamp, qualityScore);
        this.entityId = entityId;
        this.entityType = entityType;
        this.calculationMethod = calculationMethod;
        this.calculatedAt = calculatedAt != null ? calculatedAt : LocalDateTime.now();
        this.status = status != null ? status : "CALCULATED";
        this.confidenceLevel = confidenceLevel != null ? Math.max(0.0, Math.min(1.0, confidenceLevel)) : 1.0;
        this.metadata = metadata;
        this.inputRecordCount = inputRecordCount != null ? Math.max(0, inputRecordCount) : 0;
    }
    
    /**
     * Simplified constructor with essential fields only.
     * 
     * @param id Unique identifier for this result
     * @param entityId Identifier of the calculated entity
     * @param entityType Type of the calculated entity
     * @param calculationMethod Method used for calculation
     * @param status Status of the calculation
     */
    protected BaseCalculationResult(String id, String entityId, String entityType, 
                                   String calculationMethod, String status) {
        super(id);
        this.entityId = entityId;
        this.entityType = entityType;
        this.calculationMethod = calculationMethod;
        this.calculatedAt = LocalDateTime.now();
        this.status = status;
        this.confidenceLevel = 1.0;
        this.metadata = null;
        this.inputRecordCount = 0;
    }
    
    // Getters
    public String getEntityId() { return entityId; }
    public String getEntityType() { return entityType; }
    public String getCalculationMethod() { return calculationMethod; }
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public String getStatus() { return status; }
    public Double getConfidenceLevel() { return confidenceLevel; }
    public String getMetadata() { return metadata; }
    public Integer getInputRecordCount() { return inputRecordCount; }
    
    /**
     * Calculates the calculation latency in milliseconds.
     * Time difference between when data was created and when calculation was done.
     * 
     * @return Calculation latency in milliseconds
     */
    public long getCalculationLatencyMillis() {
        return java.time.Duration.between(createdAt, calculatedAt).toMillis();
    }
    
    /**
     * Checks if the calculation result is considered valid.
     * 
     * @return true if status indicates a valid result
     */
    public boolean isValidResult() {
        return Constants.STATUS_VALID.equalsIgnoreCase(status) || Constants.STATUS_CALCULATED.equalsIgnoreCase(status);
    }
    
    /**
     * Checks if the calculation has high confidence.
     * 
     * @param confidenceThreshold Minimum confidence level (0.0 to 1.0)
     * @return true if confidence level meets or exceeds threshold
     */
    public boolean hasHighConfidence(double confidenceThreshold) {
        return confidenceLevel >= confidenceThreshold;
    }
    
    /**
     * Checks if this calculation is based on sufficient input data.
     * 
     * @param minimumRecords Minimum number of records required
     * @return true if input record count meets or exceeds minimum
     */
    public boolean hasSufficientInput(int minimumRecords) {
        return inputRecordCount >= minimumRecords;
    }
    
    /**
     * Enhanced toString method including calculation-specific fields.
     */
    @Override
    public String toString() {
        return String.format(
            "%s{id='%s', entityId='%s', entityType='%s', calculationMethod='%s', " +
            "calculatedAt=%s, status='%s', confidenceLevel=%.2f, inputRecordCount=%d, " +
            "qualityScore=%.2f}",
            this.getClass().getSimpleName(),
            id,
            entityId,
            entityType,
            calculationMethod,
            calculatedAt,
            status,
            confidenceLevel,
            inputRecordCount,
            qualityScore
        );
    }
    
    /**
     * Enhanced equals method including calculation-specific fields.
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        
        BaseCalculationResult that = (BaseCalculationResult) obj;
        return Objects.equals(entityId, that.entityId) &&
               Objects.equals(entityType, that.entityType) &&
               Objects.equals(calculationMethod, that.calculationMethod) &&
               Objects.equals(calculatedAt, that.calculatedAt);
    }
    
    /**
     * Enhanced hash code including calculation-specific fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), entityId, entityType, calculationMethod, calculatedAt);
    }
    
    /**
     * Base validation that checks calculation-specific fields.
     * Subclasses should call this and add their own validation logic.
     */
    @Override
    public boolean isValid() {
        return entityId != null && !entityId.trim().isEmpty() &&
               entityType != null && !entityType.trim().isEmpty() &&
               calculationMethod != null && !calculationMethod.trim().isEmpty() &&
               calculatedAt != null &&
               status != null && !status.trim().isEmpty() &&
               confidenceLevel != null && confidenceLevel >= 0.0 && confidenceLevel <= 1.0;
    }
    
    /**
     * Abstract method to get the main calculated value.
     * Each calculation type will have a primary result value.
     * 
     * @return The main calculated value as a Double
     */
    public abstract Double getCalculatedValue();
    
    /**
     * Abstract method to get the unit of measurement for the calculated value.
     * Examples: "PERCENTAGE", "CURRENCY", "RATIO", "COUNT"
     * 
     * @return Unit of measurement
     */
    public abstract String getValueUnit();
}
