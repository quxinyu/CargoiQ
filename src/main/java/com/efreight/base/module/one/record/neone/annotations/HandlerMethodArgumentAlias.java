package com.efreight.base.module.one.record.neone.annotations;

import com.efreight.base.module.one.record.neone.resolve.HandlerMethodArgumentAliasResolver;

import java.lang.annotation.*;

/**
 * 参数别名注解
 *
 * @author Ling, Jiatong
 * Date: 2025/06/24 13:48 下午
 * @see HandlerMethodArgumentAliasResolver
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerMethodArgumentAlias {

    /**
     * 值的多个名称（别名）
     *
     * @return 值的多个名称
     */
    String[] value();

    /**
     * 默认值
     *
     * @return 默认值
     */
    String defaultValue() default "";
}
