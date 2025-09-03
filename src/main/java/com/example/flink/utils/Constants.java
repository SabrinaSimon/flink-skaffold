package com.example.flink.utils;

/**
 * Constants used throughout the Flink data processing application.
 * This class centralizes all magic numbers, default values, and configuration keys
 * to improve maintainability and reduce hardcoded values.
 * 
 * @author Data Engineering Team
 * @version 1.0
 * @since 1.0
 */
public final class Constants {
    
    // Prevent instantiation of utility class
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
    
    // ==================== FILE FORMAT CONSTANTS ====================
    
    /** CSV field delimiter */
    public static final String CSV_DELIMITER = ",";
    
    /** CSV quote character */
    public static final String CSV_QUOTE = "\"";
    
    /** Date format used in CSV files */
    public static final String CSV_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    
    /** Default encoding for file operations */
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    // ==================== BUSINESS LOGIC CONSTANTS ====================
    
    /** Default currency code */
    public static final String DEFAULT_CURRENCY = "USD";
    
    /** Minimum data quality score threshold */
    public static final double MIN_QUALITY_SCORE = 0.0;
    
    /** Maximum data quality score threshold */
    public static final double MAX_QUALITY_SCORE = 1.0;
    
    /** Default confidence level for calculations */
    public static final double DEFAULT_CONFIDENCE_LEVEL = 1.0;
    
    // ==================== PROCESSING CONSTANTS ====================
    
    /** Default parallelism for processing */
    public static final int DEFAULT_PARALLELISM = 2;
    
    /** Default window size in milliseconds (1 minute) */
    public static final long DEFAULT_WINDOW_SIZE_MS = 60_000L;
    
    /** Default checkpointing interval in milliseconds (1 minute) */
    public static final long DEFAULT_CHECKPOINT_INTERVAL_MS = 60_000L;
    
    /** Maximum number of concurrent checkpoints */
    public static final int MAX_CONCURRENT_CHECKPOINTS = 1;
    
    // ==================== FILE PATH CONSTANTS ====================
    
    /** Default input directory */
    public static final String DEFAULT_INPUT_DIR = "./data/input";
    
    /** Default output directory */
    public static final String DEFAULT_OUTPUT_DIR = "./data/output";
    
    /** Default checkpoint directory */
    public static final String DEFAULT_CHECKPOINT_DIR = "./data/checkpoints";
    
    /** Account file name */
    public static final String ACCOUNT_FILE_NAME = "accounts.csv";
    
    /** Product file name */
    public static final String PRODUCT_FILE_NAME = "products.csv";
    
    // ==================== ENTITY TYPE CONSTANTS ====================
    
    /** Account entity type identifier */
    public static final String ENTITY_TYPE_ACCOUNT = "ACCOUNT";
    
    /** Product entity type identifier */
    public static final String ENTITY_TYPE_PRODUCT = "PRODUCT";
    
    /** Concentration result entity type identifier */
    public static final String ENTITY_TYPE_CONCENTRATION = "CONCENTRATION";
    
    /** Cash result entity type identifier */
    public static final String ENTITY_TYPE_CASH = "CASH";
    
    // ==================== STATUS CONSTANTS ====================
    
    /** Valid status */
    public static final String STATUS_VALID = "VALID";
    
    /** Invalid status */
    public static final String STATUS_INVALID = "INVALID";
    
    /** Error status */
    public static final String STATUS_ERROR = "ERROR";
    
    /** Warning status */
    public static final String STATUS_WARNING = "WARNING";
    
    /** Calculated status */
    public static final String STATUS_CALCULATED = "CALCULATED";
    
    /** Pending status */
    public static final String STATUS_PENDING = "PENDING";
    
    // ==================== CALCULATION METHOD CONSTANTS ====================
    
    /** Standard calculation method */
    public static final String CALC_METHOD_STANDARD = "STANDARD_CALCULATION";
    
    /** Windowed calculation method */
    public static final String CALC_METHOD_WINDOWED = "WINDOWED_CALCULATION";
    
    /** Aggregated calculation method */
    public static final String CALC_METHOD_AGGREGATED = "AGGREGATED_CALCULATION";
    
    // ==================== ACCOUNT TYPE CONSTANTS ====================
    
    /** Checking account type */
    public static final String ACCOUNT_TYPE_CHECKING = "CHECKING";
    
    /** Savings account type */
    public static final String ACCOUNT_TYPE_SAVINGS = "SAVINGS";
    
    /** Investment account type */
    public static final String ACCOUNT_TYPE_INVESTMENT = "INVESTMENT";
    
    /** Money market account type */
    public static final String ACCOUNT_TYPE_MONEY_MARKET = "MONEY_MARKET";
    
    /** Certificate of deposit account type */
    public static final String ACCOUNT_TYPE_CD = "CD";
    
    /** Retirement account type */
    public static final String ACCOUNT_TYPE_RETIREMENT = "RETIREMENT";
    
    // ==================== RISK WEIGHT CONSTANTS ====================
    
    /** Low risk weight multiplier */
    public static final double RISK_WEIGHT_LOW = 0.8;
    
    /** Normal risk weight multiplier */
    public static final double RISK_WEIGHT_NORMAL = 1.0;
    
    /** Medium risk weight multiplier */
    public static final double RISK_WEIGHT_MEDIUM = 1.2;
    
    /** High risk weight multiplier */
    public static final double RISK_WEIGHT_HIGH = 1.5;
    
    // ==================== CONCENTRATION THRESHOLDS ====================
    
    /** Low concentration threshold */
    public static final double CONCENTRATION_THRESHOLD_LOW = 0.3;
    
    /** Medium concentration threshold */
    public static final double CONCENTRATION_THRESHOLD_MEDIUM = 0.5;
    
    /** High concentration threshold */
    public static final double CONCENTRATION_THRESHOLD_HIGH = 0.8;
    
    // ==================== CASH PROCESSING CONSTANTS ====================
    
    /** Default reserve requirement ratio (10%) */
    public static final double DEFAULT_RESERVE_RATIO = 0.1;
    
    /** Default pending transaction ratio (5%) */
    public static final double DEFAULT_PENDING_RATIO = 0.05;
    
    /** Minimum cash position threshold */
    public static final double MIN_CASH_POSITION = 0.0;
    
    // ==================== ERROR MESSAGES ====================
    
    /** Invalid configuration error message */
    public static final String ERROR_INVALID_CONFIG = "Invalid configuration provided";
    
    /** File not found error message */
    public static final String ERROR_FILE_NOT_FOUND = "Required file not found";
    
    /** Data validation error message */
    public static final String ERROR_DATA_VALIDATION = "Data validation failed";
    
    /** Processing error message */
    public static final String ERROR_PROCESSING = "Error during data processing";
    
    /** Serialization error message */
    public static final String ERROR_SERIALIZATION = "Data serialization/deserialization failed";
    
    // ==================== LOG MESSAGES ====================
    
    /** Application startup message */
    public static final String LOG_APP_STARTUP = "Flink Data Processing Application starting...";
    
    /** Application shutdown message */
    public static final String LOG_APP_SHUTDOWN = "Flink Data Processing Application shutting down...";
    
    /** Configuration loaded message */
    public static final String LOG_CONFIG_LOADED = "Configuration loaded successfully";
    
    /** Data processing started message */
    public static final String LOG_PROCESSING_STARTED = "Data processing pipeline started";
    
    /** Data processing completed message */
    public static final String LOG_PROCESSING_COMPLETED = "Data processing pipeline completed";
}
