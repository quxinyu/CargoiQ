package com.efreight.base.module.one.record.neone.parse;

/**
 * @author fu yuan hui
 * @since 2023-12-07 12:28:31 Thursday
 */
@FunctionalInterface
public interface Parse<T, R> {

    R parse(T t);
}
