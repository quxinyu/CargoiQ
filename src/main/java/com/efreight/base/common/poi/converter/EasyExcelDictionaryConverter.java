package com.efreight.base.common.poi.converter;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @author alex
 * @date 2023/05/25
 */
@Slf4j
public class EasyExcelDictionaryConverter implements Converter<LocalDate> {

    @Override
    public Class supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (null == cellData) {
            return null;
        }
        LocalDate result = null;
        if (cellData.getType() == CellDataTypeEnum.STRING) {
            String value = cellData.getStringValue();

        }
        return result;
    }

    @Override
    public WriteCellData<LocalDate> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (null == value) {
            return new WriteCellData<>();
        }
        return new WriteCellData<>(LocalDateTimeUtil.formatNormal(value));
    }
}
