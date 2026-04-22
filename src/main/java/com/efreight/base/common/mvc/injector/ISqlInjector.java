package com.efreight.base.common.mvc.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
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
public class ISqlInjector extends LampSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
        //添加你的方法
        list.add(new InsertBatchSomeColumn());
        return list;
    }
}
