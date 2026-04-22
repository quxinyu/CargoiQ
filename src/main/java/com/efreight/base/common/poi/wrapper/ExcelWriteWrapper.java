package com.efreight.base.common.poi.wrapper;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.efreight.base.common.poi.utils.ClassUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * easyexcel ExcelWriterBuilder的包装类，
 * 只对其做了默认样式的调整，以及增加了新的注解支持@Dict和@ExcelType，简化使用
 *
 * @author alex
 * @date 2022/08/11
 */
public class ExcelWriteWrapper<T> {

    /**
     * 文件名，不指定则使用uuid作为文件名
     */
    private String fileName;
    /**
     * 数据列表
     */
    private List<T> dataList;
    /**
     * 数据对象
     */
    private Class<T> clazz;

    private HttpServletResponse response;

    public ExcelWriteWrapper(HttpServletResponse response, Class<T> clazz) {
        this(response, clazz, IdUtil.fastSimpleUUID());
    }

    public ExcelWriteWrapper(HttpServletResponse response, String fileName) {
        this.response = response;
        this.fileName = fileName;
    }

    public ExcelWriteWrapper(HttpServletResponse response, Class<T> clazz, String fileName) {
        this.response = response;
        this.fileName = fileName;
        this.clazz = clazz;
    }

    public ExcelWriteWrapper(Class<T> clazz, String fileName) {
        this.fileName = fileName;
        this.clazz = clazz;
    }

    public ExcelWriteWrapper dataList(List<T> dataList) {
        this.dataList = dataList;
        return this;
    }

    /**
     * 单sheet excel导出
     *
     * @param head 自定义头
     * @param dataList 数据列表
     * @throws IOException
     */
    public void doWrite(List<List<String>> head, List<T> dataList) throws IOException {
        ExcelWriterBuilder builder = new DefaultExcelWriteBuilder(response, fileName).builder();
        if(head == null) {
            builder.excludeColumnFieldNames(ClassUtils.getIgnoreExportFieldNames(clazz));
            builder.head(clazz);
        }else {
            builder.head(head);
        }

        ExcelWriterSheetWrapper sheetWrapper = new ExcelWriterSheetWrapper(builder.build());
        sheetWrapper.sheetName(this.fileName);
        sheetWrapper.doWrite(dataList);
    }

    /**
     * 单sheet excel导出
     *
     * @param dataList 数据列表
     * @throws IOException
     */
    public void doWrite(List<T> dataList) throws IOException {
        this.doWrite(null, dataList);
    }

    /**
     * 生成单个sheet的导出流，支持多sheet导出
     *
     * @param sheetNo
     * @param builder
     */
    public void writeSheet(Integer sheetNo, ExcelWriter builder) {
        WriteSheet writeSheet = new ExcelWriterSheetWrapper(sheetNo, fileName).head(clazz).build();

        builder.write(dataList, writeSheet);
    }

    public Class getClazz() {
        return this.clazz;
    }
}
