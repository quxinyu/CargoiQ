package com.efreight.base.common.datasource.config;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.efreight.base.common.core.utils.Reflections;
import com.efreight.base.common.datasource.entity.Pagination;
import lombok.NoArgsConstructor;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * mybatis-puls 3.4.0版本以后（含）使用此版本
 * 主要是重写了PaginationInnerInterceptor中对获取IPage对象的方法，增加了Page对象从实体类中获取
 *
 * @author alex
 * @date 2021/08/25
 */
@NoArgsConstructor
public class MybatisPaginationInnerInterceptor extends PaginationInnerInterceptor {

    /**
     * 判断是否执行 Executor.query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)
     * 如果不执行query操作,则返回 Collections.emptyList()
     *
     * @param executor
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param boundSql
     * @return 新的 boundSql
     * @throws SQLException
     */
    @Override
    public boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        IPage<?> page = this.findPage(parameter).orElse(null);
        if (page == null || page.getSize() < 0 || !page.searchCount()) {
            return true;
        }

        BoundSql countSql;
        MappedStatement countMs = buildCountMappedStatement(ms, page.countId());
        if (countMs != null) {
            countSql = countMs.getBoundSql(parameter);
        } else {
            countMs = buildAutoCountMappedStatement(ms);
            String countSqlStr = autoCountSql(page, boundSql.getSql());
            PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
            countSql = new BoundSql(countMs.getConfiguration(), countSqlStr, mpBoundSql.parameterMappings(), parameter);
            PluginUtils.setAdditionalParameter(countSql, mpBoundSql.additionalParameters());
        }

        CacheKey cacheKey = executor.createCacheKey(countMs, parameter, rowBounds, countSql);
        List<Object> result = executor.query(countMs, parameter, rowBounds, resultHandler, cacheKey, countSql);
        long total = 0;
        if (CollectionUtils.isNotEmpty(result)) {
            // 个别数据库 count 没数据不会返回 0
            Object o = result.get(0);
            if (o != null) {
                total = Long.parseLong(o.toString());
            }
        }
        page.setTotal(total);
        return continuePage(page);
    }

    /**
     * Executor.query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql) 操作前置处理
     * 改改sql啥的
     *
     * @param executor
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param boundSql
     * @throws SQLException
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        IPage<?> page = this.findPage(parameter).orElse(null);
        if (null == page) {
            return;
        }

        // 处理 orderBy 拼接
        boolean addOrdered = false;
        String buildSql = boundSql.getSql();
        List<OrderItem> orders = page.orders();
        if (CollectionUtils.isNotEmpty(orders)) {
            addOrdered = true;
            buildSql = this.concatOrderBy(buildSql, orders);
        }

        // size 小于 0 且不限制返回值则不构造分页sql
        Long _limit = page.maxLimit() != null ? page.maxLimit() : maxLimit;
        if (page.getSize() < 0 && null == _limit) {
            if (addOrdered) {
                PluginUtils.mpBoundSql(boundSql).sql(buildSql);
            }
            return;
        }

        handlerLimit(page, _limit);
        IDialect dialect = findIDialect(executor);

        final Configuration configuration = ms.getConfiguration();
        DialectModel model = dialect.buildPaginationSql(buildSql, page.offset(), page.getSize());
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);

        List<ParameterMapping> mappings = mpBoundSql.parameterMappings();
        Map<String, Object> additionalParameter = mpBoundSql.additionalParameters();
        model.consumers(mappings, configuration, additionalParameter);
        mpBoundSql.sql(model.getDialectSql());
        mpBoundSql.parameterMappings(mappings);
    }

    private Optional<IPage> findPage(Object parameterObject) {
        if (parameterObject != null) {
            if (parameterObject instanceof Map) {
                Map<?, ?> parameterMap = (Map<?, ?>) parameterObject;
                for (Map.Entry entry : parameterMap.entrySet()) {
                    if (entry.getValue() != null && entry.getValue() instanceof IPage) {
                        return Optional.of((IPage) entry.getValue());
                    }
                }
            } else if (parameterObject instanceof IPage) {
                return Optional.of((IPage) parameterObject);
            } else if(ReflectUtil.hasField(parameterObject.getClass(), "page")){
                try {
                    return Optional.of((IPage<?>) ReflectUtil.getFieldValue(parameterObject, "page"));
                } catch (Exception e) {

                }
            }
        }
        return Optional.empty();
    }
}
