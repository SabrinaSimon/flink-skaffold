package com.example.flink.job;

import com.example.flink.config.FlinkConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for DataProcessingJob.
 */
@SpringBootTest
@ActiveProfiles("test")
class DataProcessingJobTest {

    @MockBean
    private FlinkConfigurationProperties config;

    @Test
    @DisplayName("Job instantiation with valid configuration")
    void testJobInstantiation() {
        // Setup mock configuration
        FlinkConfigurationProperties.Application app = new FlinkConfigurationProperties.Application();
        app.setName("test-app");
        app.setVersion("1.0.0");
        when(config.getApplication()).thenReturn(app);

        // Create job instance
        DataProcessingJob job = new DataProcessingJob(config);

        // Verify
        assertNotNull(job, "DataProcessingJob should be created successfully");
    }

    @Test
    @DisplayName("Job handles null configuration")
    void testJobWithNullConfig() {
        assertThrows(IllegalArgumentException.class, () -> {
            new DataProcessingJob(null);
        }, "Should throw IllegalArgumentException for null configuration");
    }
}
