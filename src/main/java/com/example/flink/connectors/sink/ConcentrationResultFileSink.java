package com.example.flink.connectors.sink;

import com.example.flink.domain.model.ConcentrationResult;
import com.example.flink.utils.Constants;
import org.apache.flink.api.common.serialization.Encoder;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.time.format.DateTimeFormatter;

/**
 * File sink connector specifically for ConcentrationResult data.
 * Extends the base file sink with ConcentrationResult-specific formatting.
 * Handles CSV output format for concentration calculation results.
 */
public class ConcentrationResultFileSink extends BaseFileSinkConnector<ConcentrationResult> {
    
    private static final String DEFAULT_SINK_NAME = "concentration-result-file-sink";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public ConcentrationResultFileSink(String outputPath) {
        super(outputPath, DEFAULT_SINK_NAME);
        validateAndCreateOutputPath();
    }
    
    public ConcentrationResultFileSink(String outputPath, String sinkName) {
        super(outputPath, sinkName);
        validateAndCreateOutputPath();
    }
    
    /**
     * Adds the file sink to the provided DataStream.
     * Configures the sink with proper naming and parallelism.
     * 
     * @param concentrationStream DataStream of ConcentrationResult objects
     */
    public void addSink(DataStream<ConcentrationResult> concentrationStream) {
        concentrationStream
                .sinkTo(createFileSink())
                .name(sinkName)
                .uid(sinkName + "-uid");
    }
    
    /**
     * Adds the file sink with custom parallelism.
     * 
     * @param concentrationStream DataStream of ConcentrationResult objects
     * @param parallelism desired parallelism level
     */
    public void addSink(DataStream<ConcentrationResult> concentrationStream, int parallelism) {
        concentrationStream
                .sinkTo(createFileSink())
                .setParallelism(parallelism)
                .name(sinkName)
                .uid(sinkName + "-uid");
    }
    
    @Override
    protected Encoder<ConcentrationResult> createEncoder() {
        return new ConcentrationResultCsvEncoder();
    }
    
    /**
     * CSV encoder for ConcentrationResult objects.
     * Formats concentration results as CSV with proper escaping.
     */
    private class ConcentrationResultCsvEncoder extends CsvEncoder {
        
        @Override
        protected String convertToCsv(ConcentrationResult result) {
            if (result == null) {
                return "";
            }
            
            // CSV format: entityId,entityType,concentrationValue,calculationMethod,totalExposure,riskWeight,calculatedAt,status
            return String.join(Constants.CSV_DELIMITER,
                escapeField(result.getEntityId()),
                escapeField(result.getEntityType()),
                String.valueOf(result.getConcentrationValue()),
                escapeField(result.getCalculationMethod()),
                String.valueOf(result.getTotalExposure()),
                String.valueOf(result.getRiskWeight()),
                result.getCalculatedAt() != null ? result.getCalculatedAt().format(DATE_FORMATTER) : "",
                escapeField(result.getStatus())
            );
        }
        
        /**
         * Escapes CSV field values to handle commas and quotes.
         * 
         * @param field the field value to escape
         * @return escaped field value
         */
        private String escapeField(String field) {
            if (field == null) {
                return "";
            }
            
            // Escape quotes and wrap in quotes if field contains comma or quote
            if (field.contains(Constants.CSV_DELIMITER) || field.contains("\"") || field.contains("\n")) {
                return "\"" + field.replace("\"", "\"\"") + "\"";
            }
            
            return field;
        }
    }
    
    /**
     * Creates a header line for the CSV output.
     * 
     * @return CSV header string
     */
    public static String getCsvHeader() {
        return "entityId,entityType,concentrationValue,calculationMethod,totalExposure,riskWeight,calculatedAt,status";
    }
}
