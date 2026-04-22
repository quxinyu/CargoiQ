package com.efreight.base.module.one.record.neone.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;

public class IriUtils {

    /**
     * 从URL中提取最后一个"/"后面的变量串（如446f9b29-8927-4c1b-bccd-b7f0b9723f3a）
     *
     * @param url 目标URL（格式如http://xxx/xxx/变量串）
     * @return 末尾的变量串，若URL格式异常返回null
     */
    public static String extractLastVariableFromUrl(String url) {
        // 校验URL非空且包含"/"
        if (url == null || url.isEmpty() || !url.contains("/")) {
            return null;
        }
        // 按"/"分割URL，取最后一个元素（即目标变量串）
        String[] urlParts = url.split("/");
        return urlParts[urlParts.length - 1];
    }

    /**
     * 截取 URL 的协议+主机+端口部分。
     * <p>
     * 示例：
     * <ul>
     *   <li>http://202.105.151.140:28181/api/neone → http://202.105.151.140:28181</li>
     *   <li>https://one.efreight.cn/api → https://one.efreight.cn</li>
     *   <li>http://202.100.200.73:8080/notifications → http://202.100.200.73:8080</li>
     * </ul>
     *
     * @param url 完整的 URL 字符串
     * @return 协议+主机+端口部分（例如：http://202.105.151.140:28181），如果 URL 格式错误则返回空字符串
     */
    public static String truncateBeforeLogisticsObjects(String url) {
        // 检查输入是否为空
        if (StringUtils.isBlank(url)) {
            return StringUtils.EMPTY;
        }

        try {
            // 使用 Java URL 类解析 URL
            URL urlObj = new URL(url);

            // 获取协议（http 或 https）
            String protocol = urlObj.getProtocol();

            // 获取主机（IP 或域名）
            String host = urlObj.getHost();

            // 获取端口
            int port = urlObj.getPort();

            // 构建返回的 URL
            StringBuilder result = new StringBuilder();
            result.append(protocol).append("://").append(host);

            // 如果端口存在且不是默认端口，则添加端口
            // HTTP 默认端口是 80，HTTPS 默认端口是 443
            if (port != -1) {
                if (("http".equals(protocol) && port != 80) ||
                        ("https".equals(protocol) && port != 443)) {
                    result.append(":").append(port);
                }
            }

            return result.toString();

        } catch (Exception e) {
            // URL 解析失败，返回空字符串
            return StringUtils.EMPTY;
        }
    }
}
