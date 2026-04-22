package com.efreight.base.common.datasource.interceptor;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.efreight.base.common.datasource.annotation.DataScope;
import com.efreight.base.common.datasource.handler.DataScopeHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author alex
 * @date 2023/07/03
 */
@Slf4j
public class DataScopeInterceptor extends DataPermissionInterceptor {

    private DataScopeHandler dataScopeHandler;

    public DataScopeInterceptor(DataScopeHandler dataScopeHandler) {
        super(dataScopeHandler);
        this.dataScopeHandler = dataScopeHandler;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if(this.ignore(ms.getId())) {
            return;
        }
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), ms.getId()));
    }

    protected Boolean ignore(String mappedStatementId) {
        // 检查忽略注解
        if (InterceptorIgnoreHelper.willIgnoreDataPermission(mappedStatementId)) {
            return true;
        }
        // 检查是否无效 无数据权限注解
        if (dataScopeHandler.isInvalid(mappedStatementId)) {
            return true;
        }
        // 检查是否有@DataScope注解，如无则跳过
        DataScope dataScope = dataScopeHandler.findDataScopeAnnotation(mappedStatementId);
        if (dataScope == null) {
            dataScopeHandler.addInvalidCache(mappedStatementId);
            return true;
        }
        return false;
    }

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        SqlCommandType sct = ms.getSqlCommandType();
        if (sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
            if(this.ignore(ms.getId())) {
                return;
            }
            PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
            mpBs.sql(parserMulti(mpBs.sql(), ms.getId()));
        }
    }

    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        return;
    }

    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
        return;
    }
}
