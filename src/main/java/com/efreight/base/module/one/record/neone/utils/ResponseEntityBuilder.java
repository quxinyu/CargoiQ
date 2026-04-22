package com.efreight.base.module.one.record.neone.utils;

import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author fu yuan hui
 * @since 2024-09-10 11:09:07 星期二
 */
public final class ResponseEntityBuilder implements Serializable {

    public static ResponseEntity.BodyBuilder fail() {
        return create(500);
    }

    public static ResponseEntity.BodyBuilder ok() {
        return create(200);
    }

    public static ResponseEntity.BodyBuilder create() {
        return create(201);
    }

    public static ResponseEntity.BodyBuilder create(int status) {
        return ResponseEntity
                .status(status)
                .header("version", "2.0.0-dev")
                .header("Content-Language", "en-US")
                .header("Content-Type", "application/ld+json")
                .header("Last-Modified", Instant.now().toString());
    }
}
