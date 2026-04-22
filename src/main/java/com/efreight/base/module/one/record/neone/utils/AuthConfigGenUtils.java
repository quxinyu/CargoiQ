package com.efreight.base.module.one.record.neone.utils;

import jodd.util.RandomString;

/**
 * @author fu yuan hui
 * @since 2024-11-29 13:32:01 星期五
 */
public final class AuthConfigGenUtils {


    private static final String RANDOM_KEY = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String RANDOM_KEY_ALL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


    /**
     * 生成32位的clientId
     *
     * @return String
     */
    public static String genClientId() {
        return RandomString.get().random(32, RANDOM_KEY);
    }

    /**
     * 生成64位的clientSecret， 加上时间戳扰动
     *
     * @return String
     */
    public static String genClientSecret() {
        String key1 = RandomString.get().random(32, RANDOM_KEY_ALL);
        String time = String.valueOf(System.currentTimeMillis());
        String timeRandom = time.substring(time.length() - 4);
        String key2 = RandomString.get().random(28, RANDOM_KEY_ALL);
        return String.format("%s%s%s", key1, timeRandom, key2);
    }
}
