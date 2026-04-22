package com.efreight.base.common.poi.annotation;

import com.efreight.base.common.poi.enums.ExcelTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导入、导出类型扩展
 *
 * @author alex
 * @date 2022/08/10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelType {

    ExcelTypeEnum value() default ExcelTypeEnum.ALL;
}
