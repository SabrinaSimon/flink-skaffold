package com.example.flink;

import com.example.flink.config.FlinkConfigurationProperties;
import com.example.flink.job.DataProcessingJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Spring Boot main application class for Flink Data Processing.
 * This class bootstraps the Spring context and starts the Flink job.
 */
@SpringBootApplication
@EnableConfigurationProperties(FlinkConfigurationProperties.class)
public class FlinkDataProcessingApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FlinkDataProcessingApplication.class);
    private final DataProcessingJob dataProcessingJob;
    private final FlinkConfigurationProperties config;

    public FlinkDataProcessingApplication(DataProcessingJob dataProcessingJob, 
                                        FlinkConfigurationProperties config) {
        this.dataProcessingJob = dataProcessingJob;
        this.config = config;
    }

    public static void main(String[] args) {
        SpringApplication.run(FlinkDataProcessingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting Flink Data Processing Application: {} v{}", 
                   config.getApplication().getName(), 
                   config.getApplication().getVersion());
        
        // Execute the Flink job
        dataProcessingJob.execute();
    }
}
