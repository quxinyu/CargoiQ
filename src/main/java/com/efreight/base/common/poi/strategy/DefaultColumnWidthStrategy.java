package com.efreight.base.common.poi.strategy;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 重新实现了easyexcel的自动计算列宽策略
 *
 * @author alex
 * @date 2022/08/10
 * @see com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy
 */
public class DefaultColumnWidthStrategy extends AbstractColumnWidthStyleStrategy {

    private static final int MAX_COLUMN_WIDTH = 150;
    private static final int MIN_COLUMN_WIDTH = 10;

    private final Map<Integer, Map<Integer, Integer>> cache = MapUtils.newHashMapWithExpectedSize(8);

    public DefaultColumnWidthStrategy() {

    }

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
        if (!needSetWidth) {
            return;
        }
        Map<Integer, Integer> maxColumnWidthMap = cache.computeIfAbsent(writeSheetHolder.getSheetNo(), key -> new HashMap<>(16));
        Integer columnWidth = dataLength(cellDataList, cell, isHead, head);
        if (columnWidth < 0) {
            return;
        }
        if (columnWidth < MIN_COLUMN_WIDTH) {
            columnWidth = MIN_COLUMN_WIDTH;
        }
        if (columnWidth > MAX_COLUMN_WIDTH) {
            columnWidth = MAX_COLUMN_WIDTH;
        }
        Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
        if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
            maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead, Head head) {
        if (isHead) {
            // 修改标题宽度为字符长度 * 1.5
            return cell.getStringCellValue().getBytes().length * 3 / 2;
        }
        if (head == null) {
            return 20;
        }
        Field field = head.getField();
        // 修改时间格式的为固定宽度
        if (field == null) {
            return 20;
        }
        if (field.getType() == LocalDateTime.class) {
            return 24;
        }
        if (field.getType() == LocalDate.class) {
            return 16;
        }
        WriteCellData<?> cellData = cellDataList.get(0);
        CellDataTypeEnum type = cellData.getType();
        if (type == null) {
            return -1;
        }
        switch (type) {
            case STRING:
                return cellData.getStringValue().getBytes().length;
            case BOOLEAN:
                return cellData.getBooleanValue().toString().getBytes().length;
            case NUMBER:
                return cellData.getNumberValue().toString().getBytes().length;
            default:
                return -1;
        }
    }
}
