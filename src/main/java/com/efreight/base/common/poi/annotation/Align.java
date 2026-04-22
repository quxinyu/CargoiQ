package com.efreight.base.common.poi.annotation;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对齐方式
 *
 * @author alex
 * @date 2022/08/10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Align {

    HorizontalAlignment value() default HorizontalAlignment.GENERAL;
}
