package com.efreight.base.common.core.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author alex
 * @date 2020/12/11
 */
@Slf4j
public class ResultUtil {
    /**
     * 输出json数据给前端
     *
     * @param response
     * @param obj
     */
    public static void writeJson(ServletResponse response, Object obj) {
        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
            out.write(JSON.toJSONString(obj));
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("输出json失败", e);
        }
    }
}
