package com.example.flink.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Spring Boot Configuration Properties for Flink Application.
 * This class automatically binds YAML properties to Java objects.
 * Eliminates need for custom configuration wrappers - uses Spring Boot standards.
 */
@Component
@ConfigurationProperties(prefix = "flink")
public class FlinkConfigurationProperties {

    private Application application = new Application();
    private Source source = new Source();
    private Processing processing = new Processing();
    private Sink sink = new Sink();
    private Quality quality = new Quality();
    private Metrics metrics = new Metrics();

    // Getters and setters
    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }

    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }

    public Processing getProcessing() { return processing; }
    public void setProcessing(Processing processing) { this.processing = processing; }

    public Sink getSink() { return sink; }
    public void setSink(Sink sink) { this.sink = sink; }

    public Quality getQuality() { return quality; }
    public void setQuality(Quality quality) { this.quality = quality; }

    public Metrics getMetrics() { return metrics; }
    public void setMetrics(Metrics metrics) { this.metrics = metrics; }

    // Nested configuration classes
    public static class Application {
        private String name = "flink-data-processing";
        private String version = "1.0.0";

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
    }

    public static class Source {
        private Account account = new Account();
        private Product product = new Product();

        public Account getAccount() { return account; }
        public void setAccount(Account account) { this.account = account; }
        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }

        public static class Account {
            private String path = "./data/input/accounts.csv";
            public String getPath() { return path; }
            public void setPath(String path) { this.path = path; }
        }

        public static class Product {
            private String path = "./data/input/products.csv";
            public String getPath() { return path; }
            public void setPath(String path) { this.path = path; }
        }
    }

    public static class Processing {
        private int parallelism = 4;
        private Checkpointing checkpointing = new Checkpointing();
        private Concentration concentration = new Concentration();
        private Cash cash = new Cash();

        public int getParallelism() { return parallelism; }
        public void setParallelism(int parallelism) { this.parallelism = parallelism; }
        public Checkpointing getCheckpointing() { return checkpointing; }
        public void setCheckpointing(Checkpointing checkpointing) { this.checkpointing = checkpointing; }
        public Concentration getConcentration() { return concentration; }
        public void setConcentration(Concentration concentration) { this.concentration = concentration; }
        public Cash getCash() { return cash; }
        public void setCash(Cash cash) { this.cash = cash; }

        public static class Checkpointing {
            private long interval = 60000;
            public long getInterval() { return interval; }
            public void setInterval(long interval) { this.interval = interval; }
        }

        public static class Concentration {
            private Window window = new Window();
            public Window getWindow() { return window; }
            public void setWindow(Window window) { this.window = window; }
        }

        public static class Cash {
            private Window window = new Window();
            public Window getWindow() { return window; }
            public void setWindow(Window window) { this.window = window; }
        }

        public static class Window {
            private Size size = new Size();
            public Size getSize() { return size; }
            public void setSize(Size size) { this.size = size; }

            public static class Size {
                private long ms = 60000;
                public long getMs() { return ms; }
                public void setMs(long ms) { this.ms = ms; }
            }
        }
    }

    public static class Sink {
        private int parallelism = 1;
        private Concentration concentration = new Concentration();
        private Cash cash = new Cash();

        public int getParallelism() { return parallelism; }
        public void setParallelism(int parallelism) { this.parallelism = parallelism; }
        public Concentration getConcentration() { return concentration; }
        public void setConcentration(Concentration concentration) { this.concentration = concentration; }
        public Cash getCash() { return cash; }
        public void setCash(Cash cash) { this.cash = cash; }

        public static class Concentration {
            private String path = "./data/output/concentration-results";
            public String getPath() { return path; }
            public void setPath(String path) { this.path = path; }
        }

        public static class Cash {
            private String path = "./data/output/cash-results";
            public String getPath() { return path; }
            public void setPath(String path) { this.path = path; }
        }
    }

    public static class Quality {
        private Validation validation = new Validation();

        public Validation getValidation() { return validation; }
        public void setValidation(Validation validation) { this.validation = validation; }

        public static class Validation {
            private boolean enabled = true;
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
        }
    }

    public static class Metrics {
        private boolean enabled = false;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}
