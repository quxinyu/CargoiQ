package com.efreight.base.common.core.filter;

import com.efreight.base.common.core.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author KongChen
 * @date 2023/7/7
 * <p>
 * traceId过滤器，用于日志追踪，分别在gateway/feign请求之前添加traceId，在controller结束后取消traceId
 */
@Slf4j
@Aspect
@Component
public class TraceIdFilter {
    @Pointcut("execution(* com.efreight.base..controller..*.*(..))")
    public void aroundControllers() {
    }


    @Around("aroundControllers()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 获取请求头信息,需要注意的是，请求头中的key都会转成小写
        String traceId = request.getHeader(CommonConstants.TRACE_ID);
        MDC.put(CommonConstants.TRACE_ID, traceId);
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } finally {
            MDC.remove(CommonConstants.TRACE_ID);
        }
        return obj;
    }


}
