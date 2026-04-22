package com.efreight.base.common.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author alex
 * @date 2023/07/19
 */
public class MapUtil {
    /**
     * 返回value列表
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> List<V> listValue(Map<K, V> map) {
        List<V> list = new ArrayList<>();
        map.forEach((k,v) -> list.add(v));
        return list;
    }
}
