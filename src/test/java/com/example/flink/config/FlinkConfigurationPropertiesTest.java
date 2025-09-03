package com.example.flink.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FlinkConfigurationProperties.
 * Tests configuration object creation and property setting.
 */
class FlinkConfigurationPropertiesTest {

    @Test
    void testConfigurationPropertiesCreation() {
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        assertNotNull(config);
        assertNotNull(config.getApplication());
        assertNotNull(config.getSource());
        assertNotNull(config.getProcessing());
        assertNotNull(config.getSink());
        assertNotNull(config.getQuality());
        assertNotNull(config.getMetrics());
    }

    @Test
    void testApplicationConfiguration() {
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        config.getApplication().setName("Test App");
        config.getApplication().setVersion("1.0.0");
        
        assertEquals("Test App", config.getApplication().getName());
        assertEquals("1.0.0", config.getApplication().getVersion());
    }

    @Test
    void testProcessingConfiguration() {
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        config.getProcessing().setParallelism(4);
        config.getProcessing().getCheckpointing().setInterval(10000);
        
        assertEquals(4, config.getProcessing().getParallelism());
        assertEquals(10000, config.getProcessing().getCheckpointing().getInterval());
    }

    @Test
    void testSourceConfiguration() {
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        config.getSource().getAccount().setPath("/test/accounts.csv");
        config.getSource().getProduct().setPath("/test/products.csv");
        
        assertEquals("/test/accounts.csv", config.getSource().getAccount().getPath());
        assertEquals("/test/products.csv", config.getSource().getProduct().getPath());
    }

    @Test
    void testSinkConfiguration() {
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        config.getSink().getConcentration().setPath("/test/concentration");
        config.getSink().getCash().setPath("/test/cash");
        
        assertEquals("/test/concentration", config.getSink().getConcentration().getPath());
        assertEquals("/test/cash", config.getSink().getCash().getPath());
    }

    @Test
    void testWindowConfiguration() {
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        config.getProcessing().getConcentration().getWindow().getSize().setMs(5000);
        config.getProcessing().getCash().getWindow().getSize().setMs(3000);
        
        assertEquals(5000, config.getProcessing().getConcentration().getWindow().getSize().getMs());
        assertEquals(3000, config.getProcessing().getCash().getWindow().getSize().getMs());
    }

    @Test
    void testQualityConfiguration() {
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        config.getQuality().getValidation().setEnabled(true);
        
        assertTrue(config.getQuality().getValidation().isEnabled());
    }

    @Test
    void testMetricsConfiguration() {
        FlinkConfigurationProperties config = new FlinkConfigurationProperties();
        config.getMetrics().setEnabled(true);
        
        assertTrue(config.getMetrics().isEnabled());
    }
}
