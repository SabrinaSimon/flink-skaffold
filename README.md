# Flink Data Processing Platform

Enterprise-grade Apache Flink application for real-time data processing with Spring Boot integration.

## Architecture

**Flink-Centric Clean Architecture** - Supports both batch and streaming execution modes

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│ Source          │────▶│ Processing       │────▶│ Sink            │
│ Connectors      │    │ Pipelines        │    │ Connectors      │
│ • File Sources  │    │ • Concentration  │    │ • File Sinks    │
│ • CSV Readers   │    │ • Cash Processing│    │ • CSV Writers   │
└─────────────────┘    └──────────────────┘    └─────────────────┘

Execution Modes:
├── STREAMING (default) - Real-time processing with windows
└── BATCH - Finite dataset processing for historical analysis
```

**Architectural Layers**:
- **Domain**: Core business logic (entities, value objects, domain services)
- **Job**: Flink job orchestration (execution mode agnostic)
- **Processing**: Processing workflows (pipelines, aggregators - batch + stream compatible)
- **Connectors**: Flink I/O connectors (sources, sinks, serialization)
- **Config/Utils**: Cross-cutting concerns

**Tech Stack**: Apache Flink 1.18.1, Spring Boot 3.2.0, Java 21

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+

### Local Development
```bash
# Clone and build
git clone <repository-url>
cd flink-skaffold
mvn clean package

# Run locally
java -jar target/flink-data-processing-*.jar

# Or run with Maven
mvn spring-boot:run
```

## Configuration

Configuration via `application.yml` using Spring Boot `@ConfigurationProperties`:

```yaml
flink:
  application:
    name: "flink-data-processing"
    version: "1.0.0"
  execution:
    mode: "STREAMING"              # STREAMING or BATCH
  source:
    account:
      path: "./data/input/accounts.csv"
    product:
      path: "./data/input/products.csv"
  processing:
    parallelism: 4
    checkpointing:
      interval: 60000              # Only for streaming mode
  sink:
    concentration:
      path: "./data/output/concentration-results"
    cash:
      path: "./data/output/cash-results"
```

## Project Structure

```
src/main/java/com/example/flink/
├── FlinkDataProcessingApplication.java   # Spring Boot main class
├── config/                               # Configuration management
│   └── FlinkConfigurationProperties.java
├── domain/                               # Business domain (Clean Architecture)
│   ├── model/                           # Domain entities and value objects
│   │   ├── Account.java
│   │   ├── Product.java
│   │   ├── CashResult.java
│   │   ├── ConcentrationResult.java
│   │   ├── BaseDataModel.java
│   │   └── BaseCalculationResult.java
│   └── service/                         # Domain services
│       └── BusinessRuleEngine.java
├── job/                                  # Flink job orchestration (batch + stream)
│   └── DataProcessingJob.java
├── processing/                           # Processing pipelines (mode-agnostic)
│   └── pipeline/                        # Processing pipelines
│       ├── CashProcessingPipeline.java
│       ├── ConcentrationCalculationPipeline.java
│       ├── DataProcessingPipeline.java
│       ├── CashAggregator.java
│       └── ConcentrationAggregator.java
├── connectors/                           # Flink connectors (I/O)
│   ├── source/                          # Data source connectors
│   │   ├── AccountFileSourceConnector.java
│   │   ├── ProductFileSourceConnector.java
│   │   ├── BaseFileSourceConnector.java
│   │   └── DataSourceConnector.java
│   ├── sink/                            # Data sink connectors
│   │   ├── CashResultFileSink.java
│   │   ├── ConcentrationResultFileSink.java
│   │   ├── BaseFileSinkConnector.java
│   │   ├── DataSinkConnector.java
│   │   └── DateTimeBucketAssigner.java
│   └── serialization/                   # Data format handling
│       ├── AccountDeserializer.java
│       ├── ProductDeserializer.java
│       └── DataSerializer.java
└── utils/                               # Shared utilities
    ├── Constants.java
    └── ValidationUtils.java
```

## Development

### Running Tests
```bash
mvn test
```

### Running Application
```bash
# Development mode with auto-reload
mvn spring-boot:run

# Production - Streaming mode (default)
mvn clean package
java -jar target/flink-data-processing-*.jar --spring.profiles.active=prod

# Production - Batch mode for historical data
java -jar target/flink-data-processing-*.jar --spring.profiles.active=prod --flink.execution.mode=BATCH
```

### Code Quality
- All business constants externalized to `Constants.java`
- No hardcoded values in production code
- SLF4J logging throughout
- Pure aggregators without business logic
- **Execution mode agnostic** - Works in both STREAMING and BATCH modes

## Monitoring

- **Flink UI**: http://localhost:8081
- **Health Check**: `/health` endpoint
- **Metrics**: Configurable via `flink.metrics.enabled`

## Production Deployment

### Environment Variables
```bash
SPRING_PROFILES_ACTIVE=prod
FLINK_ENV_JAVA_OPTS="-Xmx2g -Xms1g"
FLINK_EXECUTION_MODE=STREAMING    # or BATCH
```

### Production Configuration
- Uses `application-prod.yml`
- Optimized for performance and resource usage
- Enhanced security and monitoring

---

**Requirements**: Enterprise Flink application supporting both batch and streaming execution modes
**Status**: Production Ready
**Execution Modes**: 
- **STREAMING**: Real-time processing with windowed operations (default)
- **BATCH**: Finite dataset processing for historical analysis and ETL
**License**: MIT
