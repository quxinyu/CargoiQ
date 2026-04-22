package com.efreight.base.common.poi.wrapper;

import cn.hutool.core.util.CharsetUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.efreight.base.common.poi.handler.DictCellWriteHandler;
import com.efreight.base.common.poi.strategy.DefaultColumnWidthStrategy;
import com.efreight.base.common.poi.strategy.DefaultExcelStyle;
import com.efreight.base.common.poi.strategy.ExcelStyle;
import com.efreight.base.common.poi.strategy.ExcelStyleStrategy;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 默认excel装饰器
 * @author alex
 * @date 2023/07/26
 */
public class DefaultExcelWriteBuilder {

    private ExcelWriterBuilder builder;

    /**
     * 列宽策略
     */
    private WriteHandler columnWidth = new DefaultColumnWidthStrategy();
    /**
     * 行高策略
     */
    private WriteHandler rowHeight = new SimpleRowHeightStyleStrategy(DEFAULT_ROW_HEIGHT, DEFAULT_ROW_HEIGHT);
    /**
     * excel样式
     */
    private ExcelStyle excelStyle = new DefaultExcelStyle();
    /**
     * excel文件类型
     */
    public static ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;

    private static final String FILE_ENCODING = CharsetUtil.CHARSET_UTF_8.name();
    private static final String CONTENT_TYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private final static Short DEFAULT_ROW_HEIGHT = 25;

    public DefaultExcelWriteBuilder(HttpServletResponse response, String fileName) throws IOException {
        // 这里需要设置默认不关闭流，用于报错时可以返回错误代码
        this.builder = EasyExcel.write(response.getOutputStream()).autoCloseStream(false);
        // 行高策略
        this.builder.registerWriteHandler(rowHeight);
        // 列宽策略
        this.builder.registerWriteHandler(columnWidth);
        // 单元格样式策略
        this.builder.registerWriteHandler(new ExcelStyleStrategy(excelStyle));
        // 自定义数据字典转换
        this.builder.registerWriteHandler(new DictCellWriteHandler());
        this.builder.excelType(excelType);

        fileName = URLEncoder.encode(fileName, FILE_ENCODING).replaceAll("\\+", "%20");
        response.setContentType(CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding(FILE_ENCODING);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + DefaultExcelWriteBuilder.excelType.getValue());
    }

    public ExcelWriterBuilder builder() {
        return this.builder;
    }

}
