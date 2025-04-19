package com.catalyst.TicketingSystemApi.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FormatUtil {
    public static String formatName(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }

        // Trim and replace multiple spaces with single space
        String trimmed = name.trim().replaceAll("\\s+", " ");

        // Convert to title case
        return Arrays.stream(trimmed.split(" "))
                .map(word -> word.isEmpty() ? word
                        : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
