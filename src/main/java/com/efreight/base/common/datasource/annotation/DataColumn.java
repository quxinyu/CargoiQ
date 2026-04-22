package com.efreight.base.common.datasource.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限
 *
 * @author Alex
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataColumn {

    /**
     * 关联表别名
     * @return
     */
    String alias() default "";

    /**
     * 字段名
     * @return
     */
    String column();

}
