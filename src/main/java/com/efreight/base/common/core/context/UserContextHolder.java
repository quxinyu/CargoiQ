package com.efreight.base.common.core.context;


import com.efreight.base.common.core.utils.StringUtils;
import com.efreight.base.common.core.utils.text.Convert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuangyan
 * @date 2022/5/6 15:03
 */
public class UserContextHolder {

    static final ThreadLocal<Map<String, Object>> ctx = new ThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return StringUtils.cast(map.getOrDefault(key, null));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = ctx.get();
        if (map == null) {
            map = new ConcurrentHashMap<String, Object>();
            ctx.set(map);
        }
        return map;
    }

    public static void remove() {
        ctx.remove();
    }
}
