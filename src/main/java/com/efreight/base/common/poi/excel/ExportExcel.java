package com.efreight.base.common.poi.excel;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.enmus.ResultStatusCode;
import com.efreight.base.common.core.model.Result;
import com.efreight.base.common.poi.wrapper.ExcelWriteWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author alex
 * @date 2022/08/09
 */
@Slf4j
public class ExportExcel {

    /**
     * 单sheet导出
     *
     * @param response
     * @param dataList
     * @param clazz
     * @param <T>
     */
    public static <T> void write(HttpServletResponse response, List<T> dataList, Class<T> clazz) {
        write(response, dataList, clazz, null);
    }

    public static <T> void write(HttpServletResponse response, List<T> dataList, Class<T> clazz, String title) {
        Assert.notNull(clazz, "class can not be null");
        write(response, dataList, clazz, title, null);
    }

    public static <T> void write(HttpServletResponse response, List<T> dataList, Class<T> clazz, String title, List<List<String>> head) {
        try {
            Assert.notNull(response, "response can not be null");

            new ExcelWriteWrapper(response, clazz, title).doWrite(head, dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            // 重置response
            response.reset();

            // 错误时不返回文件流，统一返回错误代码
            ServletUtil.write(response,
                    JSONObject.toJSONString(Result.result(ResultStatusCode.EXCEL_EXPORT_ERROR)),
                    ContentType.build(ContentType.JSON, CharsetUtil.CHARSET_UTF_8));
        }
    }

    public static <T> void write(HttpServletResponse response, Supplier<List<T>> supplier, Class<T> clazz) {
        write(response, supplier.get(), clazz);
    }

    public static <T> void write(HttpServletResponse response, Supplier<ExcelWriteWrapper> supplier, List<T> dataList) {
        try {
            supplier.get().doWrite(dataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            // 重置response
            response.reset();

            // 错误时不返回文件流，统一返回错误代码
            ServletUtil.write(response,
                    JSONObject.toJSONString(Result.result(ResultStatusCode.EXCEL_EXPORT_ERROR)),
                    ContentType.build(ContentType.JSON, CharsetUtil.CHARSET_UTF_8));
        }
    }

    /**
     * 支持自定义动态头
     * @param response
     * @param dataList
     * @param title
     * @param head
     */
    public static void write(HttpServletResponse response, List dataList, String title, List<List<String>> head) {
        write(response, dataList, null, title, head);
    }

}
