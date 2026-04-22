package com.efreight.base.common.poi.annotation;

import com.efreight.base.common.poi.enums.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EasyExcel自定义注解
 *
 * @author wangchengfei
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    /**
     * 导出字段标题（需要添加批注请用“**”分隔，标题**批注，仅对导出模板有效）
     */
    String title();

    /**
     * 字段类型，默认为导入、导出
     */
    ExcelTypeEnum type() default ExcelTypeEnum.ALL;

    /**
     * 水平对齐方式，默认对齐方式为：文本类型居左，数字、日期、时间类型居右，布尔类型居中
     */
    HorizontalAlignment align() default HorizontalAlignment.GENERAL;

    /**
     * 导出字段字段排序（升序）
     */
    int sort() default -1;

    /**
     * 列宽，以字符宽度的1/256为单位，-1表示自动设置列宽
     *
     * @return
     * @see org.apache.poi.ss.usermodel.Sheet setColumnWidth()
     */
    int width() default -1;

    /**
     * 如果是字典类型，请设置字典的type值
     */
    String dictType() default "";

    /**
     * 反射类型
     */
    Class<?> fieldType() default Class.class;

}
