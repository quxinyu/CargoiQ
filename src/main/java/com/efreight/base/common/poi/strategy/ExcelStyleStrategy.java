package com.efreight.base.common.poi.strategy;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;

/**
 * @author alex
 * @date 2022/08/10
 */
public class ExcelStyleStrategy extends AbstractCellStyleStrategy {

    private ExcelStyle excelStyle;

    public ExcelStyleStrategy(ExcelStyle excelStyle) {
        this.excelStyle = excelStyle;
    }

    @Override
    protected void setHeadCellStyle(CellWriteHandlerContext context) {
        if (stopProcessing(context) || excelStyle == null) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(excelStyle.headStyle(context.getHeadData()), cellData.getOrCreateStyle());
    }

    @Override
    protected void setContentCellStyle(CellWriteHandlerContext context) {
        if (stopProcessing(context) || excelStyle == null) {
            return;
        }
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(excelStyle.bodyStyle(context.getCell()), cellData.getOrCreateStyle());
    }

    protected boolean stopProcessing(CellWriteHandlerContext context) {
        return context.getFirstCellData() == null;
    }
}
