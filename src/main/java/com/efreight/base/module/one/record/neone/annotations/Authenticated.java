package com.efreight.base.module.one.record.neone.annotations;

import com.efreight.base.module.one.record.neone.interceptor.OneRecordServerAuthenticationInterceptor;

import java.lang.annotation.*;

/**
 * 认证注解
 *
 * @author fu yuan hui
 * @since 2024-08-29 10:52:29 星期四
 * @see OneRecordServerAuthenticationInterceptor
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {
}
