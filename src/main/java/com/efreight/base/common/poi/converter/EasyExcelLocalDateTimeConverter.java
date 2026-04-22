package com.efreight.base.common.poi.converter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author alex
 * @date 2023/05/25
 */
@Slf4j
public class EasyExcelLocalDateTimeConverter implements Converter<LocalDateTime> {

    @Override
    public Class supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (null == cellData) {
            return null;
        }
        LocalDateTime result = null;
        if (cellData.getType() == CellDataTypeEnum.STRING) {
            String value = cellData.getStringValue();

            result = LocalDateTimeUtil.parse(value, DatePattern.NORM_DATETIME_FORMATTER);
        }
        return result;
    }

    @Override
    public WriteCellData<LocalDateTime> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (null == value) {
            return new WriteCellData<>();
        }
        return new WriteCellData<>(LocalDateTimeUtil.formatNormal(value));
    }
}
