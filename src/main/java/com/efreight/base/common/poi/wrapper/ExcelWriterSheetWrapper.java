package com.efreight.base.common.poi.wrapper;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.util.Collection;

/**
 * easyexcel ExcelWriterSheetBuilder的包装类
 *
 * @author alex
 * @date 2022/08/11
 */
public class ExcelWriterSheetWrapper extends AbstractExcelWriteWrapper<ExcelWriterSheetWrapper, ExcelWriterSheetBuilder> {

    private ExcelWriterSheetBuilder builder;

    public ExcelWriterSheetWrapper() {
        this.builder = EasyExcel.writerSheet();
    }

    public ExcelWriterSheetWrapper(Integer sheetNo, String sheetName) {
        this.builder = EasyExcel.writerSheet(sheetNo, sheetName);
    }

    public ExcelWriterSheetWrapper(ExcelWriter excelWriter) {
        this.builder = new ExcelWriterSheetBuilder(excelWriter);
    }

    public ExcelWriterSheetWrapper sheetName(String sheetName) {
        builder.sheetName(sheetName);
        return this;
    }

    public ExcelWriterSheetWrapper sheetNo(Integer sheetNo) {
        builder.sheetNo(sheetNo);
        return this;
    }

    public ExcelWriterSheetWrapper doWrite(Collection<?> data) {
        builder.doWrite(data);
        return this;
    }

    public WriteSheet build() {
        return this.builder.build();
    }

    @Override
    protected ExcelWriterSheetBuilder parameter() {
        return this.builder;
    }
}
