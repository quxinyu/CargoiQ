package com.efreight.base.module.one.record.neone.utils;

import com.efreight.base.common.core.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author fu yuan hui
 * @since 2025-04-15 18:18:43 星期二
 */
public final class Iso8601UtcTime2LocalDateTimeUtils {

    public static LocalDateTime parse(String iso8601UtcTime) {
        if (StringUtils.isBlank(iso8601UtcTime)) {
            return null;
        }
        // 定义解析格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

        // 将字符串解析为 ZonedDateTime（带时区信息）
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(iso8601UtcTime, formatter.withZone(ZoneOffset.UTC));

        // 如果需要转换为 LocalDateTime（不带时区信息）
        return zonedDateTime.toLocalDateTime();
    }
}
