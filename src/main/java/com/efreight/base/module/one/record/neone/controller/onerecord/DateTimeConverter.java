// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.controller.onerecord;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for converting string representations of dates to Instant.
 */
public class DateTimeConverter {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_INSTANT;

    /**
     * Converts a string to an Instant. Returns null if the string is null or empty.
     *
     * @param dateTimeStr the date time string to convert
     * @return the converted Instant, or null if the input is null or empty
     */
    public static Instant convert(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }

        try {
            return Instant.from(ISO_FORMATTER.parse(dateTimeStr));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateTimeStr, e);
        }
    }

    private DateTimeConverter() {
        // Utility class - prevent instantiation
    }
}
