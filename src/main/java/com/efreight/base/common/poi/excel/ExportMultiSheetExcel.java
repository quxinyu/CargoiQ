package com.efreight.base.common.poi.excel;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.enmus.ResultStatusCode;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.common.poi.utils.ClassUtils;
import com.efreight.base.common.poi.wrapper.DefaultExcelWriteBuilder;
import com.efreight.base.common.poi.wrapper.ExcelWriteWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author alex
 * @date 2023/07/26
 */
@Slf4j
public class ExportMultiSheetExcel {

    private List<ExcelWriteWrapper> excelWriteWrapperList = new ArrayList<>();

    public <T> ExportMultiSheetExcel add(String sheetName, Class<T> clazz, List<T> dataList) {
        this.excelWriteWrapperList.add(new ExcelWriteWrapper(clazz, sheetName).dataList(dataList));
        return this;
    }

    public void doWrite(String fileName, HttpServletResponse response) {
        ExcelWriter excelWriter = null;
        try {
            ExcelWriterBuilder excelWriterBuilder = new DefaultExcelWriteBuilder(response, fileName)
                    .builder()
                    .excludeColumnFieldNames(ClassUtils.getIgnoreExportFieldNames(this.excelWriteWrapperList.stream().map(ExcelWriteWrapper::getClazz).collect(Collectors.toList())));

            excelWriter = excelWriterBuilder.build();
            for (int i = 0; i < excelWriteWrapperList.size(); i++) {
                excelWriteWrapperList.get(i).writeSheet(i, excelWriter);
            }

        }catch (Exception e) {
            // 重置response
            response.reset();

            // 错误时不返回文件流，统一返回错误代码
            ServletUtil.write(response,
                    JSONObject.toJSONString(Result.result(ResultStatusCode.EXCEL_EXPORT_ERROR)),
                    ContentType.build(ContentType.JSON, CharsetUtil.CHARSET_UTF_8));
        }finally {
            if(excelWriter != null) {
                excelWriter.finish();
            }
        }
    }
}
