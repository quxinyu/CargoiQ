package com.efreight.base.common.datasource.interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.efreight.base.common.core.constant.CommonConstants;
import com.efreight.base.common.datasource.config.properties.CanalProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.MDC;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

/**
 * @author KongChen
 * @date 2023/7/10
 */
@Slf4j
public class TraceIdInterceptor extends JsqlParserSupport implements InnerInterceptor {
    private static final String TRACE_ID_COLUMN = "trace_id";

    private final CanalProperties canalProperties;

    public TraceIdInterceptor(CanalProperties canalProperties) {
        super();
        this.canalProperties = canalProperties;
    }

    @SneakyThrows
    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
        PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
        MappedStatement ms = mpSh.mappedStatement();
        final BoundSql boundSql = mpSh.boundSql();
        SqlCommandType sct = ms.getSqlCommandType();
        DatabaseMetaData metaData = connection.getMetaData();
        if (canalProperties.containsDatabase(metaData.getURL()) && StrUtil.isNotEmpty(MDC.get(CommonConstants.TRACE_ID))) {
            if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
                PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
                if (sct == SqlCommandType.DELETE) {
                    Statement statement = CCJSqlParserUtil.parse(mpBs.sql());
                    beforeDelete((Delete) statement, ms, boundSql, connection);
                }
                mpBs.sql(parserMulti(mpBs.sql(), null));
            }
        }
    }

    @Override
    public String parserMulti(String sql, Object obj) {
        try {
            // fixed github pull/295
            StringBuilder sb = new StringBuilder();
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            int i = 0;
            for (Statement statement : statements.getStatements()) {
                if (i > 0) {
                    sb.append(StringPool.SEMICOLON);
                }
                sb.append(processParser(statement, i, sql, obj));
                i++;
            }
            return sb.toString();
        } catch (JSQLParserException e) {
            throw ExceptionUtils.mpe("Failed to process, Error SQL: %s", e.getCause(), sql);
        }
    }

    /**
     * 执行 SQL 解析
     *
     * @param statement JsqlParser Statement
     * @return sql
     */
    @Override
    protected String processParser(Statement statement, int index, String sql, Object obj) {
        if (statement instanceof Insert) {
            this.processInsert((Insert) statement, index, sql, obj);
        } else if (statement instanceof Select) {
            this.processSelect((Select) statement, index, sql, obj);
        } else if (statement instanceof Update) {
            this.processUpdate((Update) statement, index, sql, obj);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete) statement, index, sql, obj);
        }
        sql = statement.toString();
        return sql;
    }

    /**
     * insert 语句处理
     * 在 update 中增加 trace_id = xxx
     */
    @SneakyThrows
    @Override
    protected void processInsert(Insert insert, int index, String sql, Object obj) {
        String tableName = insert.getTable().getName();
        if (canalProperties.ignoreTable(tableName)) {
            log.info("tableName:{}，跳过，insert不处理traceId", tableName);
            return;
        }
        List<Column> columns = insert.getColumns();
        if (CollectionUtils.isEmpty(columns)) {
            // 针对不给列名的insert 不处理
            return;
        }
        if (ignoreColumns(insert.getColumns(), TRACE_ID_COLUMN)) {
            // 针对已给出traceId的不处理
            return;
        }
        columns.add(new Column(TRACE_ID_COLUMN));
        List<Expression> duplicateUpdateColumns = insert.getDuplicateUpdateExpressionList();
        if (CollectionUtils.isNotEmpty(duplicateUpdateColumns)) {
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new StringValue(TRACE_ID_COLUMN));
            equalsTo.setRightExpression(new StringValue(MDC.get(CommonConstants.TRACE_ID)));
            duplicateUpdateColumns.add(equalsTo);
        }

        if (insert.getItemsList() != null) {
            ItemsList itemsList = insert.getItemsList();
            if (itemsList instanceof MultiExpressionList) {
                ((MultiExpressionList) itemsList).getExpressionLists().forEach(el -> el.getExpressions().add(new StringValue(MDC.get(CommonConstants.TRACE_ID))));
            } else {
                ((ExpressionList) itemsList).getExpressions().add(new StringValue(MDC.get(CommonConstants.TRACE_ID)));
            }
        }
    }

    /**
     * update 语句处理
     * 在 update 中增加 trace_id = xxx
     */
    @SneakyThrows
    @Override
    protected void processUpdate(Update update, int index, String sql, Object obj) {
        String tableName = update.getTable().getName();
        if (canalProperties.ignoreTable(tableName)) {
            log.info("tableName:{}，跳过，update不处理traceId", tableName);
            return;
        }
        List<Column> columns = update.getUpdateSets().get(0).getColumns();
        if (CollectionUtils.isEmpty(columns)) {
            // 针对不给列名的update 不处理
            return;
        }
        if (ignoreColumns(update.getUpdateSets().get(0).getColumns(), TRACE_ID_COLUMN)) {
            // 针对已给出traceId的不处理
            return;
        }
        update.addUpdateSet(new Column(TRACE_ID_COLUMN), new StringValue(MDC.get(CommonConstants.TRACE_ID)));
    }

    /**
     * delete 语句处理
     * 删除前执行更新traceId
     */
    @SneakyThrows
    protected void beforeDelete(Delete deleteStmt, MappedStatement mappedStatement, BoundSql boundSql, Connection connection) {
        String tableName = deleteStmt.getTable().getName();
        if (canalProperties.ignoreTable(tableName)) {
            log.info("tableName:{}，跳过，delete不处理traceId", tableName);
            return;
        }
        Table table = deleteStmt.getTable();
        Expression where = deleteStmt.getWhere();
        Update update = new Update();
        update.setTable(table);
        update.setWhere(where);
        update.addUpdateSet(new Column(TRACE_ID_COLUMN), new StringValue(MDC.get(CommonConstants.TRACE_ID)));

        BoundSql boundSql4Update = new BoundSql(mappedStatement.getConfiguration(), update.toString(),
                boundSql.getParameterMappings(), boundSql.getParameterObject());
        MetaObject metaObject = SystemMetaObject.forObject(boundSql);
        Map<String, Object> additionalParameters = (Map<String, Object>) metaObject.getValue("additionalParameters");
        if (additionalParameters != null && !additionalParameters.isEmpty()) {
            for (Map.Entry<String, Object> ety : additionalParameters.entrySet()) {
                boundSql4Update.setAdditionalParameter(ety.getKey(), ety.getValue());
            }
        }
        PreparedStatement statement = connection.prepareStatement(update.toString());
        DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql4Update.getParameterObject(), boundSql4Update);
        parameterHandler.setParameters(statement);
        int result = statement.executeUpdate();

        log.info("before delete table: {}, sql: {}, result = {}", table.getName(), update, result);
    }

    @Override
    protected void processDelete(Delete delete, int index, String sql, Object obj) {
    }

    private boolean ignoreColumns(List<Column> columns, String columnName) {
        return columns.stream().map(Column::getColumnName).anyMatch(i -> i.equalsIgnoreCase(columnName));
    }

}

