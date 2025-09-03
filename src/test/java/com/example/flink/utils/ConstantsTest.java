package com.example.flink.utils;

import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Constants class.
 * Validates that all constants are properly defined and have expected values.
 */
class ConstantsTest {

    @Test
    void testConstantsClassCannotBeInstantiated() {
        // Test that Constants constructor throws exception when invoked via reflection
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            // Use reflection to access private constructor
            java.lang.reflect.Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
        
        // Verify the root cause is UnsupportedOperationException
        assertTrue(exception.getCause() instanceof UnsupportedOperationException);
        assertEquals("Constants class cannot be instantiated", exception.getCause().getMessage());
    }

    @Test
    void testFileFormatConstants() {
        assertEquals(",", Constants.CSV_DELIMITER);
        assertEquals("\"", Constants.CSV_QUOTE);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", Constants.CSV_DATE_FORMAT);
        assertEquals("UTF-8", Constants.DEFAULT_ENCODING);
    }

    @Test
    void testBusinessLogicConstants() {
        assertEquals("USD", Constants.DEFAULT_CURRENCY);
        assertEquals(0.0, Constants.MIN_QUALITY_SCORE);
        assertEquals(1.0, Constants.MAX_QUALITY_SCORE);
        assertEquals(1.0, Constants.DEFAULT_CONFIDENCE_LEVEL);
    }

    @Test
    void testProcessingConstants() {
        assertEquals(2, Constants.DEFAULT_PARALLELISM);
        assertEquals(60_000L, Constants.DEFAULT_WINDOW_SIZE_MS);
        assertEquals(60_000L, Constants.DEFAULT_CHECKPOINT_INTERVAL_MS);
        assertEquals(1, Constants.MAX_CONCURRENT_CHECKPOINTS);
    }

    @Test
    void testStatusConstants() {
        assertEquals("VALID", Constants.STATUS_VALID);
        assertEquals("INVALID", Constants.STATUS_INVALID);
        assertEquals("ERROR", Constants.STATUS_ERROR);
        assertEquals("WARNING", Constants.STATUS_WARNING);
        assertEquals("CALCULATED", Constants.STATUS_CALCULATED);
        assertEquals("PENDING", Constants.STATUS_PENDING);
    }

    @Test
    void testRiskWeightConstants() {
        assertEquals(0.8, Constants.RISK_WEIGHT_LOW);
        assertEquals(1.0, Constants.RISK_WEIGHT_NORMAL);
        assertEquals(1.2, Constants.RISK_WEIGHT_MEDIUM);
        assertEquals(1.5, Constants.RISK_WEIGHT_HIGH);
    }

    @Test
    void testConcentrationThresholds() {
        assertEquals(0.3, Constants.CONCENTRATION_THRESHOLD_LOW);
        assertEquals(0.5, Constants.CONCENTRATION_THRESHOLD_MEDIUM);
        assertEquals(0.8, Constants.CONCENTRATION_THRESHOLD_HIGH);
    }

    @Test
    void testCashProcessingConstants() {
        assertEquals(0.1, Constants.DEFAULT_RESERVE_RATIO);
        assertEquals(0.05, Constants.DEFAULT_PENDING_RATIO);
        assertEquals(0.0, Constants.MIN_CASH_POSITION);
    }

    @Test
    void testFilePathConstants() {
        assertEquals("./data/input", Constants.DEFAULT_INPUT_DIR);
        assertEquals("./data/output", Constants.DEFAULT_OUTPUT_DIR);
        assertEquals("./data/checkpoints", Constants.DEFAULT_CHECKPOINT_DIR);
        assertEquals("accounts.csv", Constants.ACCOUNT_FILE_NAME);
        assertEquals("products.csv", Constants.PRODUCT_FILE_NAME);
    }

    @Test
    void testEntityTypeConstants() {
        assertEquals("ACCOUNT", Constants.ENTITY_TYPE_ACCOUNT);
        assertEquals("PRODUCT", Constants.ENTITY_TYPE_PRODUCT);
        assertEquals("CONCENTRATION", Constants.ENTITY_TYPE_CONCENTRATION);
        assertEquals("CASH", Constants.ENTITY_TYPE_CASH);
    }

    @Test
    void testAccountTypeConstants() {
        assertEquals("CHECKING", Constants.ACCOUNT_TYPE_CHECKING);
        assertEquals("SAVINGS", Constants.ACCOUNT_TYPE_SAVINGS);
        assertEquals("INVESTMENT", Constants.ACCOUNT_TYPE_INVESTMENT);
        assertEquals("MONEY_MARKET", Constants.ACCOUNT_TYPE_MONEY_MARKET);
        assertEquals("CD", Constants.ACCOUNT_TYPE_CD);
        assertEquals("RETIREMENT", Constants.ACCOUNT_TYPE_RETIREMENT);
    }

    @Test
    void testCalculationMethodConstants() {
        assertEquals("STANDARD_CALCULATION", Constants.CALC_METHOD_STANDARD);
        assertEquals("WINDOWED_CALCULATION", Constants.CALC_METHOD_WINDOWED);
        assertEquals("AGGREGATED_CALCULATION", Constants.CALC_METHOD_AGGREGATED);
    }

    @Test
    void testErrorMessageConstants() {
        assertNotNull(Constants.ERROR_INVALID_CONFIG);
        assertNotNull(Constants.ERROR_FILE_NOT_FOUND);
        assertNotNull(Constants.ERROR_DATA_VALIDATION);
        assertNotNull(Constants.ERROR_PROCESSING);
        assertNotNull(Constants.ERROR_SERIALIZATION);
        
        assertFalse(Constants.ERROR_INVALID_CONFIG.isEmpty());
        assertFalse(Constants.ERROR_FILE_NOT_FOUND.isEmpty());
        assertFalse(Constants.ERROR_DATA_VALIDATION.isEmpty());
        assertFalse(Constants.ERROR_PROCESSING.isEmpty());
        assertFalse(Constants.ERROR_SERIALIZATION.isEmpty());
    }

    @Test
    void testLogMessageConstants() {
        assertNotNull(Constants.LOG_APP_STARTUP);
        assertNotNull(Constants.LOG_APP_SHUTDOWN);
        assertNotNull(Constants.LOG_CONFIG_LOADED);
        assertNotNull(Constants.LOG_PROCESSING_STARTED);
        assertNotNull(Constants.LOG_PROCESSING_COMPLETED);
        
        assertFalse(Constants.LOG_APP_STARTUP.isEmpty());
        assertFalse(Constants.LOG_APP_SHUTDOWN.isEmpty());
        assertFalse(Constants.LOG_CONFIG_LOADED.isEmpty());
        assertFalse(Constants.LOG_PROCESSING_STARTED.isEmpty());
        assertFalse(Constants.LOG_PROCESSING_COMPLETED.isEmpty());
    }
}
