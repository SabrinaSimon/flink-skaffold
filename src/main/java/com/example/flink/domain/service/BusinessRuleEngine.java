package com.example.flink.domain.service;

/**
 * Generic interface for business rule validation and application.
 * This interface provides a contract for implementing complex business logic
 * that can be applied consistently across different data processing pipelines.
 * 
 * @param <T> The data type on which business rules will be applied
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public interface BusinessRuleEngine<T> {
    
    /**
     * Validates if the provided data meets all business rules.
     * This method should check all applicable business constraints without modifying the data.
     * 
     * @param data The data object to validate
     * @return ValidationResult containing validation status and details
     */
    ValidationResult validate(T data);
    
    /**
     * Applies business transformations to the data based on configured rules.
     * This method may modify the data according to business logic.
     * 
     * @param data The data object to transform
     * @return The transformed data object
     * @throws BusinessRuleException if rule application fails
     */
    T applyRules(T data) throws BusinessRuleException;
    
    /**
     * Calculates business metrics or derived values from the data.
     * Used for creating calculated fields, aggregations, or business indicators.
     * 
     * @param data The data object to analyze
     * @return BusinessMetrics object containing calculated values
     */
    BusinessMetrics calculateMetrics(T data);
    
    /**
     * Returns the priority level of this rule engine.
     * Higher priority rules are applied first when multiple engines are chained.
     * 
     * @return Priority level (1 = highest, 10 = lowest)
     */
    int getPriority();
    
    /**
     * Returns the name of this business rule engine.
     * Used for identification and logging purposes.
     * 
     * @return Rule engine name
     */
    String getRuleEngineName();
    
    /**
     * Returns a description of the business rules implemented by this engine.
     * Useful for documentation and compliance reporting.
     * 
     * @return Description of business rules
     */
    String getRuleDescription();
    
    /**
     * Checks if this rule engine is applicable to the provided data type.
     * Allows for conditional rule application based on data characteristics.
     * 
     * @param data The data object to check
     * @return true if rules should be applied, false otherwise
     */
    boolean isApplicable(T data);
    
    /**
     * Validation result containing status and details.
     */
    class ValidationResult {
        private final boolean isValid;
        private final String message;
        private final String ruleViolation;
        private final SeverityLevel severity;
        
        public ValidationResult(boolean isValid, String message, String ruleViolation, SeverityLevel severity) {
            this.isValid = isValid;
            this.message = message;
            this.ruleViolation = ruleViolation;
            this.severity = severity;
        }
        
        public boolean isValid() { return isValid; }
        public String getMessage() { return message; }
        public String getRuleViolation() { return ruleViolation; }
        public SeverityLevel getSeverity() { return severity; }
    }
    
    /**
     * Business metrics container for calculated values.
     */
    class BusinessMetrics {
        // Placeholder for metric fields - implement based on specific business needs
        // Examples: riskScore, concentrationRatio, complianceRating, etc.
        
        /**
         * TODO: Implement specific metric fields based on business requirements
         * Example implementations:
         * - private double riskScore;
         * - private double concentrationRatio;
         * - private ComplianceRating complianceRating;
         * - private Map<String, Object> customMetrics;
         */
    }
    
    /**
     * Severity levels for validation results.
     */
    enum SeverityLevel {
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }
    
    /**
     * Custom exception for business rule processing errors.
     */
    class BusinessRuleException extends Exception {
        public BusinessRuleException(String message) {
            super(message);
        }
        
        public BusinessRuleException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
