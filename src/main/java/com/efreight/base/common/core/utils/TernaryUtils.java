package com.efreight.base.common.core.utils;

import java.util.function.Supplier;

/**
 * 三元表达式工具类，将三元表达式转化为静态工具类，可增加可读性
 * @author alex
 * @date 2023/07/11
 */
public class TernaryUtils {

    /**
     *
     * @param condition 条件
     * @param trueValue 条件为true时返回的值
     * @param falseValue 条件为false时返回的值
     * @param <T>
     * @return
     */
    public static <T> T ifElse(boolean condition, T trueValue, T falseValue) {
        return condition ? trueValue : falseValue;
    }

    /**
     *
     * @param condition 条件
     * @param trueValue 条件为true时返回的值
     * @param falseValue 条件为false时返回的值
     * @param <T>
     * @return
     */
    public static <T> T ifElse(boolean condition, Supplier<T> trueValue, Supplier<T> falseValue) {
        return condition ? trueValue.get() : falseValue.get();
    }

    /**
     *
     * @param condition 条件
     * @param trueValue 条件为true时返回的值
     * @param falseValue 条件为false时返回的值
     * @param <T>
     * @return
     */
    public static <T> T ifElse(Supplier<Boolean> condition, Supplier<T> trueValue, Supplier<T> falseValue) {
        return condition.get() ? trueValue.get() : falseValue.get();
    }
}
