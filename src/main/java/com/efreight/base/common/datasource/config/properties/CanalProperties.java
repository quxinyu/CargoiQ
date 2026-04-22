package com.efreight.base.common.datasource.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KongChen
 * @date 2023/7/18
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "canal")
public class CanalProperties {
    private List<String> databaseNames = new ArrayList<>();
    private List<String> ignoreTableNames = new ArrayList<>();
    private List<String> ignoreTableRegexList = new ArrayList<>();

    /**
     * 用于判断配置文件中配置的库
     *
     * @param url
     * @return
     */
    public boolean containsDatabase(String url) {
        for (String tableName : databaseNames) {
            if (url.contains(tableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用于判断是否在配置文件中有忽略相应的table
     *
     * @param tableName
     * @return
     */
    public boolean ignoreTable(String tableName) {
        if (ignoreTableNames.contains(tableName)) {
            return true;
        }
        for (String regex : ignoreTableRegexList) {
            String pattern = "^" + regex + "$";
            if (tableName.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}
