package com.example.flink.model.base;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base class for all data model objects in the Flink processing pipeline.
 * Provides common fields and functionality that all business entities should have.
 * This follows the DRY principle and ensures consistency across all data models.
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseDataModel {
    
    /**
     * Unique identifier for this entity.
     * Should be populated for all records to enable tracking and correlation.
     */
    protected final String id;
    
    /**
     * Timestamp when this record was created in the source system.
     * Used for temporal analysis and data lineage tracking.
     */
    protected final LocalDateTime createdAt;
    
    /**
     * Timestamp when this record was last updated in the source system.
     * Important for change data capture and incremental processing.
     */
    protected final LocalDateTime updatedAt;
    
    /**
     * Version number for optimistic concurrency control.
     * Incremented each time the record is modified.
     */
    protected final Long version;
    
    /**
     * Source system identifier where this data originated.
     * Useful for multi-source data integration and troubleshooting.
     */
    protected final String sourceSystem;
    
    /**
     * Processing timestamp when this record entered the Flink pipeline.
     * Used for measuring processing latency and SLA monitoring.
     */
    protected final LocalDateTime processingTimestamp;
    
    /**
     * Data quality score indicating the reliability of this record.
     * Scale: 0.0 (poor) to 1.0 (excellent quality)
     */
    protected final Double qualityScore;
    
    /**
     * Constructor for creating a new BaseDataModel instance.
     * 
     * @param id Unique identifier for this entity
     * @param createdAt Timestamp when record was created
     * @param updatedAt Timestamp when record was last updated
     * @param version Version number for concurrency control
     * @param sourceSystem Source system identifier
     * @param processingTimestamp Processing timestamp
     * @param qualityScore Data quality score (0.0 to 1.0)
     */
    protected BaseDataModel(String id, LocalDateTime createdAt, LocalDateTime updatedAt,
                           Long version, String sourceSystem, LocalDateTime processingTimestamp,
                           Double qualityScore) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
        this.sourceSystem = sourceSystem;
        this.processingTimestamp = processingTimestamp != null ? processingTimestamp : LocalDateTime.now();
        this.qualityScore = qualityScore != null ? Math.max(0.0, Math.min(1.0, qualityScore)) : 1.0;
    }
    
    /**
     * Default constructor with minimal required fields.
     * 
     * @param id Unique identifier for this entity
     */
    protected BaseDataModel(String id) {
        this(id, LocalDateTime.now(), LocalDateTime.now(), 1L, "UNKNOWN", LocalDateTime.now(), 1.0);
    }
    
    // Getters
    public String getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }
    public String getSourceSystem() { return sourceSystem; }
    public LocalDateTime getProcessingTimestamp() { return processingTimestamp; }
    public Double getQualityScore() { return qualityScore; }
    
    /**
     * Calculates the age of this record in milliseconds.
     * 
     * @return Age in milliseconds since creation
     */
    public long getAgeInMillis() {
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toMillis();
    }
    
    /**
     * Checks if this record is considered fresh based on the provided threshold.
     * 
     * @param maxAgeMillis Maximum age in milliseconds to be considered fresh
     * @return true if record is fresh, false otherwise
     */
    public boolean isFresh(long maxAgeMillis) {
        return getAgeInMillis() <= maxAgeMillis;
    }
    
    /**
     * Checks if this record has high data quality.
     * 
     * @param qualityThreshold Minimum quality score threshold (0.0 to 1.0)
     * @return true if quality score meets or exceeds threshold
     */
    public boolean hasHighQuality(double qualityThreshold) {
        return qualityScore >= qualityThreshold;
    }
    
    /**
     * Returns a string representation suitable for logging and debugging.
     * Subclasses should override this to include specific field information.
     * 
     * @return String representation of the base fields
     */
    @Override
    public String toString() {
        return String.format(
            "%s{id='%s', createdAt=%s, updatedAt=%s, version=%d, sourceSystem='%s', processingTimestamp=%s, qualityScore=%.2f}",
            this.getClass().getSimpleName(),
            id,
            createdAt,
            updatedAt,
            version,
            sourceSystem,
            processingTimestamp,
            qualityScore
        );
    }
    
    /**
     * Equals implementation based on ID and version.
     * Subclasses may override this for more specific equality checks.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BaseDataModel that = (BaseDataModel) obj;
        return Objects.equals(id, that.id) && Objects.equals(version, that.version);
    }
    
    /**
     * Hash code implementation based on ID and version.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }
    
    /**
     * Abstract method to be implemented by subclasses for validation.
     * Should check if all required fields are properly populated.
     * 
     * @return true if the entity is valid, false otherwise
     */
    public abstract boolean isValid();
    
    /**
     * Abstract method to return the entity type name.
     * Used for polymorphic processing and routing.
     * 
     * @return Entity type identifier
     */
    public abstract String getEntityType();
}
