package com.efreight.base.module.one.record.neone.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author fu yuan hui
 * @since 2024-08-20 14:08:29 星期二
 */
@Slf4j
public final class UrlFormatUtils {


    /**
     * <pre>
     *
     *     解析前: https:///localhost:8909/abc///nacos/
     *     解析后: https://localhost:8909/abc/nacos/
     *
     *     解析前: https://192.168.3.12:8809//nacos/refresh
     *     解析后: https://192.168.3.12:8809/nacos/refresh
     *
     *     解析前: http://localhost:8876//event/push//listener///
     *     解析后: http://localhost:8876/event/push/listener/
     *
     *     解析前: https://localhost:8909/abc/nacos
     *     解析后: https://localhost:8909/abc/nacos
     * </pre>
     * @param url：原始的url
     * @return 解析后的url
     */
    public static String resolveUrl(String url) {
        // 使用正则表达式替换连续的两个斜杠，但保留 https:// 中的两个斜杠
        return url.replaceAll("(?<!http:|https:)/{2,}", "/");
    }

    public static boolean validUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>>. URL 非法：url: {}", url, e);
            return false;
        }
    }
}
