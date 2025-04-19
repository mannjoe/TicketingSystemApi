package com.catalyst.TicketingSystemApi.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;

public class DateParserUtil {

    private static final List<DateTimeFormatter> SUPPORTED_FORMATTERS = Arrays.asList(
            // ISO formats
            DateTimeFormatter.ISO_INSTANT,
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ISO_DATE,
            DateTimeFormatter.ISO_LOCAL_DATE,

            // Common custom formats
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd MMM yyyy"),
            DateTimeFormatter.ofPattern("dd MMMM yyyy")
    );

    public static LocalDate parseToLocalDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty");
        }

        // Try all supported formatters
        for (DateTimeFormatter formatter : SUPPORTED_FORMATTERS) {
            try {
                // Handle formats with timezones
                if (formatter.equals(DateTimeFormatter.ISO_INSTANT) ||
                        formatter.equals(DateTimeFormatter.ISO_DATE_TIME) ||
                        dateString.contains("T")) {
                    TemporalAccessor temporal = formatter.parse(dateString);
                    if (temporal.isSupported(ChronoField.INSTANT_SECONDS)) {
                        Instant instant = Instant.from(temporal);
                        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
                    }
                    if (temporal.isSupported(ChronoField.OFFSET_SECONDS)) {
                        OffsetDateTime odt = OffsetDateTime.from(temporal);
                        return odt.toLocalDate();
                    }
                    if (temporal.isSupported(ChronoField.EPOCH_DAY)) {
                        return LocalDate.from(temporal);
                    }
                }
                // For simple date formats
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }

        // If none of the formatters worked, try some heuristic parsing
        try {
            // Handle Unix timestamp (seconds or milliseconds)
            if (dateString.matches("^\\d+$")) {
                long timestamp = Long.parseLong(dateString);
                // Check if it's seconds or milliseconds
                if (timestamp > 1_000_000_000_000L) { // Probably milliseconds
                    return Instant.ofEpochMilli(timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                } else { // Probably seconds
                    return Instant.ofEpochSecond(timestamp)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                }
            }
        } catch (NumberFormatException ignored) {
            // Not a numeric timestamp
        }

        throw new DateTimeParseException(
                "Unable to parse date: " + dateString +
                        ". Supported formats: ISO dates, yyyy-MM-dd, MM/dd/yyyy, etc.",
                dateString, 0);
    }
}