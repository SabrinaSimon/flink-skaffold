package com.example.flink.job;

import com.example.flink.config.FlinkConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DataProcessingJob.
 */
class DataProcessingJobTest {

    @Test
    @DisplayName("DataProcessingJob can be instantiated with valid configuration")
    void testJobInstantiation() {
        // Create a valid configuration
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        config.getApplication().setName("Test App");
        config.getApplication().setVersion("1.0.0");
        config.getSource().getAccount().setPath("/test/accounts.csv");
        config.getSource().getProduct().setPath("/test/products.csv");
        config.getSink().getConcentration().setPath("/test/concentration");
        config.getSink().getCash().setPath("/test/cash");
        
        // Test that DataProcessingJob can be created
        DataProcessingJob job = new DataProcessingJob(config);
        assertNotNull(job);
    }

    @Test
    @DisplayName("DataProcessingJob handles null configuration gracefully")
    void testJobWithNullConfig() {
        // Test that DataProcessingJob handles null configuration
        assertThrows(IllegalArgumentException.class, () -> {
            new DataProcessingJob(null);
        });
    }
}
