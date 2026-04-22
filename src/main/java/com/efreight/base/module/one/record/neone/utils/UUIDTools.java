package com.efreight.base.module.one.record.neone.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fu yuan hui
 * @since 2024-05-30 11:37:18 Thursday
 */
public class UUIDTools {

    @SuppressWarnings("all")
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }

    private static final AtomicLong sequence = new AtomicLong(0);
    private static final long SEQUENCE_BITS = 12L;
    private static final long SEQUENCE_MASK = (1L << SEQUENCE_BITS) - 1L; // 4096
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS;

    @SuppressWarnings("all")
    public static String generateSimpleUUID() {
        String uuid = UUID.randomUUID().toString();

        long timestamp = System.currentTimeMillis();
        long seq = sequence.getAndIncrement() & SEQUENCE_MASK;

        // 组合时间戳和序列号，类似雪花算法
        long combined = (timestamp << TIMESTAMP_SHIFT) | seq;
        String combined36 = Long.toString(combined, 36);

        // 确保有足够长度并截取需要的部分
        combined36 = String.format("%8s", combined36).replace(' ', '0');
        String timeSuffix = combined36.substring(0, 5);
        String seqSuffix = combined36.substring(5, 8);

        String suffix = timeSuffix.substring(3);
        String prefix = timeSuffix.substring(0, 3);

        StringBuilder builder = new StringBuilder(uuid);
        StringBuilder replace = builder.replace(5, 8, prefix).replace(28, 30, suffix);

        // 用序列号替换特定位置
        String result = replace.toString();
        for (int i = 0; i < 3 && i < seqSuffix.length(); i++) {
            int[] positions = {15, 20, 25};
            result = result.substring(0, positions[i]) + seqSuffix.charAt(i) + result.substring(positions[i] + 1);
        }
        return result;
    }

    @Deprecated
    @SuppressWarnings("all")
    public static String generateSimpleUUID2() {
        String uuid = UUID.randomUUID().toString();

        //加上时间扰动
        String time = String.valueOf(System.currentTimeMillis());
        String timeSuffix = time.substring(time.length() - 5);

        String suffix = timeSuffix.substring(3);
        String perfix = timeSuffix.replaceAll(suffix, "");

        StringBuilder builder = new StringBuilder(uuid);
        StringBuilder replace = builder.replace(5, 8, perfix).replace(28, 30, suffix);
        return replace.toString();
    }

    @SuppressWarnings("all")
    public static String generate512BitsUUID() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(generateSafeUUID());
        }

        return sb.toString();
    }


    @SuppressWarnings("all")
    public static String generateUUID(boolean toUpper) {
        if (toUpper) {
            return generateUUID().toUpperCase();
        }

        return generateUUID();
    }

    /**
     * 长度是18位，为了尽可能的不重复，这里加入时间戳进行扰动
     *
     * @return
     */
    @SuppressWarnings("all")
    public static String generateSafeUUID() {
        return generateSafeUUID(false);
    }

    @SuppressWarnings("all")
    public static String generateSafeUUID(boolean toUpper) {
        String uuid = generate(toUpper) + generate(toUpper);
        return uuid.substring(0, uuid.length() - 4);
    }

    private static String generate(boolean toUpper) {
        String uuid = generateUUID(toUpper);
        //5位
        String prefix = uuid.substring(0, 5);
        //5位
        String suffix = uuid.substring(uuid.length() - 5);

        String now = String.valueOf(System.currentTimeMillis());
        //2位
        String random1 = now.substring((now.length() - 5), (now.length() - 3));

        //3位
        String random2 = now.substring((now.length() - 3));

        //3位
        String substring = generateUUID().substring(10, 13);

        return prefix + random1 + substring + random2 + suffix;
    }
}
