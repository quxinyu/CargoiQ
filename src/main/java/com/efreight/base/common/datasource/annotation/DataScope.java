package com.efreight.base.common.datasource.annotation;

import com.efreight.base.common.core.constant.CommonConstants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限组
 *
 * @author Alex
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * 数据权限字段，支持多字段组合
     * @return
     */
    DataColumn[] value();

    /**
     * 多字段关联逻辑
     * @return
     */
    String logic() default CommonConstants.DataScope.LOGIC_AND;
}
