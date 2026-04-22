package com.efreight.base.common.mvc.injector;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义sql 注入器
 *
 * @author efreight
 * @date 2022-09-19
 */
@Slf4j
public class LampSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        log.info("加载mybatis plus自定义方法");
        //增加自定义方法
        methodList.add(new InsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE));
        methodList.add(new UpdateAllById(null));
        return methodList;
    }
}
