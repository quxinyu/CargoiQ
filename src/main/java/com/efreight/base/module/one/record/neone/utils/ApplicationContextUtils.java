package com.efreight.base.module.one.record.neone.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring ApplicationContext工具类
 *
 * @author fu yuan hui
 * @since 2024-05-16 17:28:57 Thursday
 */
@Component
public final class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.ctx = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) ctx.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) throws BeansException {
        return ctx.getBean(clz);
    }
}
