package com.efreight.base.common.poi.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author alex
 * @date 2022/08/09
 */
public class DefaultExcelStyle implements ExcelStyle {
    @Override
    public WriteCellStyle headStyle(Head head) {
        WriteCellStyle headCellStyle = new WriteCellStyle();
        // 对齐方式：居中
        headCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 边框
        headCellStyle.setBorderBottom(BorderStyle.THIN);
        headCellStyle.setBorderLeft(BorderStyle.THIN);
        headCellStyle.setBorderRight(BorderStyle.THIN);
        headCellStyle.setBorderTop(BorderStyle.THIN);
        // 背景色填充策略
        headCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 前景色
        headCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return headCellStyle;
    }

    @Override
    public WriteCellStyle bodyStyle(Cell cell) {
        WriteCellStyle bodyCellStyle = new WriteCellStyle();
        // 默认对齐方式：水平对齐
        bodyCellStyle.setHorizontalAlignment(HorizontalAlignment.GENERAL);
        // 边框
        bodyCellStyle.setBorderBottom(BorderStyle.THIN);
        bodyCellStyle.setBorderLeft(BorderStyle.THIN);
        bodyCellStyle.setBorderRight(BorderStyle.THIN);
        bodyCellStyle.setBorderTop(BorderStyle.THIN);

        return bodyCellStyle;
    }
}
