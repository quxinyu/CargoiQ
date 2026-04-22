package com.efreight.base.common.datasource.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.efreight.base.common.core.constant.CommonConstants;
import com.efreight.base.common.core.model.LoginUser;
import com.efreight.base.common.datasource.config.properties.CanalProperties;
import com.efreight.base.common.datasource.handler.DataScopeHandler;
import com.efreight.base.common.datasource.interceptor.DataScopeInterceptor;
import com.efreight.base.common.datasource.interceptor.TraceIdInterceptor;
import com.efreight.base.common.satoken.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

/**
 * mybatis plus相关配置
 *
 * @author alex
 * @date 2020/06/03
 */
@Slf4j
@EnableTransactionManagement
@RequiredArgsConstructor
@Configuration
@MapperScan("com.efreight.base.**.mapper*")
public class MyBatisPlusConfig implements MetaObjectHandler {

    private final CanalProperties canalProperties;

    /**
     * mybatis plus拦截器配置
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 数据权限拦截器
        // interceptor.addInnerInterceptor(new DataPermissionInterceptor(new DataScopeHandler()));
        interceptor.addInnerInterceptor(new DataScopeInterceptor(new DataScopeHandler()));
        // 自定义实现的分页处理器
        interceptor.addInnerInterceptor(new MybatisPaginationInnerInterceptor());
        // traceId处理
        interceptor.addInnerInterceptor(new TraceIdInterceptor(canalProperties));
        return interceptor;
    }

    /**
     * 新增数据默认字段填充策略
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LoginUser user = SecurityUtil.getUser();
        // todo 后续接登陆可删除
        if (ObjectUtils.isEmpty(user)) {
            user = new LoginUser();
            user.setNickName("1");
        }
        if (user != null) {
            this.strictInsertFill(metaObject, "createBy", String.class, user.getNickName());
            this.strictInsertFill(metaObject, "updateBy", String.class, user.getNickName());
            this.strictInsertFill(metaObject, "operator", String.class, user.getNickName());
            this.strictInsertFill(metaObject, "flowCreateUser", String.class, user.getNickName());
        }
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "pushTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "delFlag", Integer.class, CommonConstants.DEL_FLAG_NORMAL);
    }

    /**
     * 更新数据默认字段填充策略
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 强制更新
        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);

        LoginUser user = SecurityUtil.getUser();
        if (user != null) {
            setFieldValByName("updateBy", user.getNickName(), metaObject);
        }
    }
}
