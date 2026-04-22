package com.efreight.base.module.one.record.neone.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

/**
 * @author fu yuan hui
 * @since 2024-09-11 11:53:09 星期三
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NotifyHelper {

    private final OkHttpClient okHttpClient;

    public void notification() {

    }
}
