//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateTimeConverter {

    public static Instant convert(String dateTimeString) {
        if (StringUtils.isBlank(dateTimeString))
            return null;

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyyMMdd'T'HHmmss'Z'")
                .withZone(ZoneId.of("UTC"));
        TemporalAccessor dt = formatter.parse(dateTimeString);
        return Instant.from(dt);
    }
}
