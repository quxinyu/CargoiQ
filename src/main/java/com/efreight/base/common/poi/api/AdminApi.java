package com.efreight.base.common.poi.api;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.efreight.base.common.core.exception.EftException;
import com.efreight.base.common.core.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Properties;


/**
 * @author alex
 * @date 2022/08/12
 */
@Slf4j
public class AdminApi {

    public static JSONArray getDictByType(String dictType) {
        try {
            Environment env = SpringContextHolder.getApplicationContext().getEnvironment();
            String serverAddr = env.getProperty("spring.cloud.nacos.discovery.server-addr");
            String namespace = env.getProperty("spring.cloud.nacos.discovery.namespace");

            Properties properties = new Properties();
            properties.setProperty("serverAddr", serverAddr);
            properties.setProperty("namespace", namespace);
            NamingService naming = NamingFactory.createNamingService(properties);
            List<Instance> instanceList = naming.getAllInstances("base-modules-admin");
            if (CollectionUtil.isNotEmpty(instanceList)) {
                Instance instance = instanceList.get(RandomUtil.randomInt(instanceList.size()));
                String url = String.format("http://%s:%s/admin/system/dict/data/dictType/%s", instance.getIp(), instance.getPort() + "", dictType);
                String resultStr = HttpUtil.get(url);

                JSONObject result = JSONObject.parseObject(resultStr);
                return result.getJSONArray("data");
            }

        } catch (Exception e) {

        }
        throw new EftException("调用admin服务查询字典失败！");
    }
}
