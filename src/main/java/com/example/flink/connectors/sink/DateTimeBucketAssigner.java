package com.example.flink.connectors.sink;

import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Bucket assigner that organizes output files by date and time.
 * Creates a hierarchical directory structure for better file organization.
 * Format: year=YYYY/month=MM/day=DD/hour=HH/
 */
public class DateTimeBucketAssigner<T> implements BucketAssigner<T, String> {
    
    private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd");
    private static final DateTimeFormatter HOUR_FORMATTER = DateTimeFormatter.ofPattern("HH");
    
    @Override
    public String getBucketId(T element, Context context) {
        LocalDateTime now = LocalDateTime.now();
        
        return "year=" + now.format(YEAR_FORMATTER) +
               "/month=" + now.format(MONTH_FORMATTER) +
               "/day=" + now.format(DAY_FORMATTER) +
               "/hour=" + now.format(HOUR_FORMATTER);
    }
    
    @Override
    public SimpleVersionedStringSerializer getSerializer() {
        return SimpleVersionedStringSerializer.INSTANCE;
    }
}
