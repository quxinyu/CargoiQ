package com.efreight.base.common.datasource.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.efreight.base.common.core.constant.CommonConstants;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.model.LoginUser;
import com.efreight.base.common.core.utils.StreamUtils;
import com.efreight.base.common.core.utils.StringUtils;
import com.efreight.base.common.datasource.annotation.DataColumn;
import com.efreight.base.common.datasource.annotation.DataScope;
import com.efreight.base.common.datasource.enums.DataScopeType;
import com.efreight.base.common.satoken.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据权限过滤
 *
 * @author Alex
 */
@Slf4j
public class DataScopeHandler implements DataPermissionHandler {

    /**
     * 方法或类(名称) 与 注解的映射关系缓存
     */
    private final Map<String, DataScope> dataPermissionCacheMap = new ConcurrentHashMap<>();

    /**
     * 无效注解方法缓存用于快速返回
     */
    private final Set<String> invalidCacheSet = new ConcurrentHashSet<>();

    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        DataScope dataScope = findDataScopeAnnotation(mappedStatementId);
        if (dataScope == null) {
            invalidCacheSet.add(mappedStatementId);
            return where;
        }
        LoginUser currentUser = SecurityUtil.getUser();
        // 如果不是代理则直接跳过
        if (ObjectUtil.isNull(currentUser) || currentUser.getIsAgent() == CommonConstants.BOOLEAN_NO) {
            return where;
        }
        String dataFilterSql = buildDataFilter(dataScope);
        if (StringUtils.isBlank(dataFilterSql)) {
            return where;
        }
        try {
            Expression expression = CCJSqlParserUtil.parseExpression(dataFilterSql);
            // 数据权限使用单独的括号 防止与其他条件冲突
            Parenthesis parenthesis = new Parenthesis(expression);
            if (ObjectUtil.isNotNull(where)) {
                return new AndExpression(where, parenthesis);
            } else {
                return parenthesis;
            }
        } catch (JSQLParserException e) {
            throw new EftException("数据权限解析异常 => " + e.getMessage());
        }
    }

    /**
     * 构造数据过滤sql
     */
    private String buildDataFilter(DataScope dataScope) {
        // 更新或删除需满足所有条件
        LoginUser user = SecurityUtil.getUser();
        Set<String> conditions = new HashSet<>();

        // 获取角色权限泛型
        DataScopeType type = DataScopeType.findCode(user.getDataScope());
        if (ObjectUtil.isNull(type)) {
            throw new EftException("角色数据范围异常 => " + user.getDataScope());
        }
        // 全部数据权限直接返回
        if (type == DataScopeType.ALL) {
            return StrUtil.EMPTY;
        }
        for (DataColumn dataColumn : dataScope.value()) {
            // 解析sql模板并填充
            String sql = null;
            String alias = dataColumn.alias();
            if(StringUtils.isNotEmpty(alias)) {
                alias = alias + StrUtil.DOT;
            }
            if(type == DataScopeType.SELF) {
                sql = StrFormatter.format(type.getTemplate(), alias, dataColumn.column(), user.getAgentId());
            }else if(type == DataScopeType.CUSTOM) {
                sql = StrFormatter.format(type.getTemplate(), alias, dataColumn.column(), StrUtil.join(StrUtil.COMMA, user.getDataScopeAgentList()));
            }
            conditions.add(dataScope.logic() + sql);
        }

        if (CollUtil.isNotEmpty(conditions)) {
            String sql = StreamUtils.join(conditions, Function.identity(), StrUtil.EMPTY);
            return sql.substring(dataScope.logic().length());
        }
        return StrUtil.EMPTY;
    }

    public DataScope findDataScopeAnnotation(String mappedStatementId) {
        StringBuilder sb = new StringBuilder(mappedStatementId);
        int index = sb.lastIndexOf(StrUtil.DOT);
        String clazzName = sb.substring(0, index);
        String methodName = sb.substring(index + 1, sb.length());
        Class<?> clazz = ClassUtil.loadClass(clazzName);
        List<Method> methods = Arrays.stream(ClassUtil.getDeclaredMethods(clazz))
                .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
        DataScope dataPermission;
        // 获取方法注解
        for (Method method : methods) {
            dataPermission = dataPermissionCacheMap.get(mappedStatementId);
            if (ObjectUtil.isNotNull(dataPermission)) {
                return dataPermission;
            }
            if (AnnotationUtil.hasAnnotation(method, DataScope.class)) {
                dataPermission = AnnotationUtil.getAnnotation(method, DataScope.class);
                dataPermissionCacheMap.put(mappedStatementId, dataPermission);
                return dataPermission;
            }
        }
        dataPermission = dataPermissionCacheMap.get(clazz.getName());
        if (ObjectUtil.isNotNull(dataPermission)) {
            return dataPermission;
        }

        // 获取类注解
        if (AnnotationUtil.hasAnnotation(clazz, DataScope.class)) {
            dataPermission = AnnotationUtil.getAnnotation(clazz, DataScope.class);
            dataPermissionCacheMap.put(clazz.getName(), dataPermission);
            return dataPermission;
        }
        return null;
    }

    /**
     * 是否为无效方法 无数据权限
     */
    public boolean isInvalid(String mappedStatementId) {
        return invalidCacheSet.contains(mappedStatementId);
    }

    /**
     * 添加缓存
     * @param mappedStatementId
     */
    public void addInvalidCache(String mappedStatementId){
        invalidCacheSet.add(mappedStatementId);
    }
}
