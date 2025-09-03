package com.example.flink.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data model representing processed concentration calculation results.
 * This is the output of the concentration calculation pipeline.
 */
public class ConcentrationResult {
    
    private final String entityId;
    private final String entityType;
    private final double concentrationValue;
    private final String calculationMethod;
    private final double totalExposure;
    private final double riskWeight;
    private final LocalDateTime calculatedAt;
    private final String status;
    
    public ConcentrationResult() {
        this(null, null, 0.0, null, 0.0, 0.0, null, null);
    }
    
    public ConcentrationResult(String entityId, String entityType, double concentrationValue,
                              String calculationMethod, double totalExposure, double riskWeight,
                              LocalDateTime calculatedAt, String status) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.concentrationValue = concentrationValue;
        this.calculationMethod = calculationMethod;
        this.totalExposure = totalExposure;
        this.riskWeight = riskWeight;
        this.calculatedAt = calculatedAt;
        this.status = status;
    }
    
    public String getEntityId() {
        return entityId;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public double getConcentrationValue() {
        return concentrationValue;
    }
    
    public String getCalculationMethod() {
        return calculationMethod;
    }
    
    public double getTotalExposure() {
        return totalExposure;
    }
    
    public double getRiskWeight() {
        return riskWeight;
    }
    
    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcentrationResult that = (ConcentrationResult) o;
        return Double.compare(that.concentrationValue, concentrationValue) == 0 &&
               Double.compare(that.totalExposure, totalExposure) == 0 &&
               Double.compare(that.riskWeight, riskWeight) == 0 &&
               Objects.equals(entityId, that.entityId) &&
               Objects.equals(entityType, that.entityType) &&
               Objects.equals(calculationMethod, that.calculationMethod) &&
               Objects.equals(calculatedAt, that.calculatedAt) &&
               Objects.equals(status, that.status);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(entityId, entityType, concentrationValue, calculationMethod, 
                           totalExposure, riskWeight, calculatedAt, status);
    }
    
    @Override
    public String toString() {
        return "ConcentrationResult{" +
               "entityId='" + entityId + '\'' +
               ", entityType='" + entityType + '\'' +
               ", concentrationValue=" + concentrationValue +
               ", calculationMethod='" + calculationMethod + '\'' +
               ", totalExposure=" + totalExposure +
               ", riskWeight=" + riskWeight +
               ", calculatedAt=" + calculatedAt +
               ", status='" + status + '\'' +
               '}';
    }
}
