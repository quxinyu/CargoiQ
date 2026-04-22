package com.efreight.base.common.poi.utils;

import cn.hutool.core.lang.Assert;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.poi.annotation.ExcelType;
import com.efreight.base.common.poi.enums.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author alex
 * @date 2022/08/11
 */
@Slf4j
public class ClassUtils {

    /**
     * 获取所有导出列对象
     *
     * @param clazz
     * @return
     */
    public static List<Field> getExportFields(Class clazz) {
        Assert.notNull(clazz, "class can not be null");
        Field[] fields = clazz.getDeclaredFields();
        Assert.notEmpty(fields, "class must have field");

        List<Field> exportFields = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            // 添加对@ExcelType注解的支持，排除非导出类型字段
            ExcelType excelType = field.getAnnotation(ExcelType.class);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);

            boolean hasPropertyAndNotHasIgnore = excelProperty != null && excelIgnore == null;
            boolean exportExcelType = excelType == null || excelType.value() != ExcelTypeEnum.ONLY_IMPORT;
            if (hasPropertyAndNotHasIgnore && exportExcelType) {
                exportFields.add(field);
            }
        }
        Assert.notEmpty(exportFields, "export fields cant not be empty");
        return exportFields;
    }

    public static Set<String> getIgnoreExportFieldNames(Collection<Class> clazzList) {
        Set<String> fullList = new HashSet<>();
        for (Class clazz : clazzList) {
            fullList.addAll(getIgnoreExportFieldNames(clazz));
        }
        return fullList;
    }

    public static Set<String> getIgnoreExportFieldNames(Class clazz) {
        Assert.notNull(clazz, "class can not be null");
        Field[] fields = clazz.getDeclaredFields();
        Assert.notEmpty(fields, "class must have field");

        Set<String> ignoreList = new HashSet<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            // 添加对@ExcelType注解的支持，排除非导出类型字段
            ExcelType excelType = field.getAnnotation(ExcelType.class);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);

            boolean notHasPropertyOrHasIgnore = excelProperty == null || excelIgnore != null;
            boolean exportExcelType = excelType != null && excelType.value() == ExcelTypeEnum.ONLY_IMPORT;
            if (notHasPropertyOrHasIgnore || exportExcelType) {
                ignoreList.add(field.getName());
            }
        }
        log.info("ignore fields: {}", JSONObject.toJSONString(ignoreList));
        return ignoreList;
    }
}
