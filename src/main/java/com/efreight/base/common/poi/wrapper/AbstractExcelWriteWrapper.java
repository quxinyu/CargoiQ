package com.efreight.base.common.poi.wrapper;

import com.alibaba.excel.write.builder.AbstractExcelWriterParameterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.efreight.base.common.poi.utils.ClassUtils;

import java.util.Collection;

/**
 * @author alex
 * @date 2022/08/11
 */
public abstract class AbstractExcelWriteWrapper<T extends AbstractExcelWriteWrapper, C extends AbstractExcelWriterParameterBuilder> {

    public T head(Class<?> clazz) {
        parameter().excludeColumnFieldNames(ClassUtils.getIgnoreExportFieldNames(clazz));
        parameter().head(clazz);
        return self();
    }

    public T registerWriteHandler(WriteHandler writeHandler) {
        parameter().registerWriteHandler(writeHandler);
        return self();
    }

    public T excludeColumnFieldNames(Collection<String> fieldNames) {
        parameter().excludeColumnFieldNames(fieldNames);
        return self();
    }

    /**
     * 动态表头
     *
     * @param fieldNames
     * @return
     */
    public T dynamicHeaders(Collection<String> fieldNames) {
        parameter().includeColumnFieldNames(fieldNames);
        return self();
    }

    protected T self() {
        return (T) this;
    }

    protected abstract C parameter();
}
