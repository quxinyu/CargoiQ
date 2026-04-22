package com.efreight.base.common.poi.strategy;

import org.apache.poi.ss.usermodel.Cell;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;

/**
 * @author alex
 * @date 2022/08/09
 */
public interface ExcelStyle {
    /**
     * 头部样式
     *
     * @return
     */
    WriteCellStyle headStyle(Head head);

    /**
     * 内容样式
     *
     * @return
     */
    WriteCellStyle bodyStyle(Cell cell);
}
