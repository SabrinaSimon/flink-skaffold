package com.example.flink.connectors.sink;

import com.example.flink.domain.model.CashResult;
import com.example.flink.utils.Constants;
import org.apache.flink.api.common.serialization.Encoder;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.time.format.DateTimeFormatter;

/**
 * File sink connector specifically for CashResult data.
 * Extends the base file sink with CashResult-specific formatting.
 * Handles CSV output format for cash processing results.
 */
public class CashResultFileSink extends BaseFileSinkConnector<CashResult> {
    
    private static final String DEFAULT_SINK_NAME = "cash-result-file-sink";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public CashResultFileSink(String outputPath) {
        super(outputPath, DEFAULT_SINK_NAME);
        validateAndCreateOutputPath();
    }
    
    public CashResultFileSink(String outputPath, String sinkName) {
        super(outputPath, sinkName);
        validateAndCreateOutputPath();
    }
    
    /**
     * Adds the file sink to the provided DataStream.
     * Configures the sink with proper naming and parallelism.
     * 
     * @param cashStream DataStream of CashResult objects
     */
    public void addSink(DataStream<CashResult> cashStream) {
        cashStream
                .sinkTo(createFileSink())
                .name(sinkName)
                .uid(sinkName + "-uid");
    }
    
    /**
     * Adds the file sink with custom parallelism.
     * 
     * @param cashStream DataStream of CashResult objects
     * @param parallelism desired parallelism level
     */
    public void addSink(DataStream<CashResult> cashStream, int parallelism) {
        cashStream
                .sinkTo(createFileSink())
                .setParallelism(parallelism)
                .name(sinkName)
                .uid(sinkName + "-uid");
    }
    
    @Override
    protected Encoder<CashResult> createEncoder() {
        return new CashResultCsvEncoder();
    }
    
    /**
     * CSV encoder for CashResult objects.
     * Formats cash results as CSV with proper escaping.
     */
    private class CashResultCsvEncoder extends CsvEncoder {
        
        @Override
        protected String convertToCsv(CashResult result) {
            if (result == null) {
                return "";
            }
            
            // CSV format: accountId,transactionId,cashPosition,availableCash,pendingAmount,currency,calculatedAt,status
            return String.join(Constants.CSV_DELIMITER,
                escapeField(result.getAccountId()),
                escapeField(result.getTransactionId()),
                String.valueOf(result.getCashPosition()),
                String.valueOf(result.getAvailableCash()),
                String.valueOf(result.getPendingAmount()),
                escapeField(result.getCurrency()),
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
        return "accountId,transactionId,cashPosition,availableCash,pendingAmount,currency,calculatedAt,status";
    }
}
