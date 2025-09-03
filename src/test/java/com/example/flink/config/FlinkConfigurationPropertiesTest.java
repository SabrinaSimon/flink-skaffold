package com.example.flink.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FlinkConfigurationProperties.
 * Tests Spring Boot native YAML configuration binding.
 */
@SpringBootTest
@ActiveProfiles("test")
class FlinkConfigurationPropertiesTest {

    @Autowired
    private FlinkConfigurationProperties config;

    @Test
    void testApplicationNameIsConfigured() {
        assertNotNull(config.getApplication().getName());
        assertFalse(config.getApplication().getName().isEmpty());
    }

    @Test
    void testApplicationVersionIsConfigured() {
        assertNotNull(config.getApplication().getVersion());
        assertFalse(config.getApplication().getVersion().isEmpty());
    }

    @Test
    void testProcessingParallelismIsPositive() {
        assertTrue(config.getProcessing().getParallelism() > 0);
    }

    @Test
    void testCheckpointingIntervalIsPositive() {
        assertTrue(config.getProcessing().getCheckpointing().getInterval() > 0);
    }

    @Test
    void testWindowSizesArePositive() {
        assertTrue(config.getProcessing().getConcentration().getWindow().getSize().getMs() > 0);
        assertTrue(config.getProcessing().getCash().getWindow().getSize().getMs() > 0);
    }

    @Test
    void testFilePathsAreConfigured() {
        assertNotNull(config.getSource().getAccount().getPath());
        assertNotNull(config.getSource().getProduct().getPath());
        assertNotNull(config.getSink().getConcentration().getPath());
        assertNotNull(config.getSink().getCash().getPath());
    }

    @Test
    void testValidationIsConfigurable() {
        // Should not throw exception
        boolean validationEnabled = config.getQuality().getValidation().isEnabled();
        // Test passes if no exception is thrown
    }

    @Test
    void testMetricsIsConfigurable() {
        // Should not throw exception
        boolean metricsEnabled = config.getMetrics().isEnabled();
        // Test passes if no exception is thrown
    }
}
