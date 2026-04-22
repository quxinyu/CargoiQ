package com.efreight.base.module.one.record.neone.interceptor;

import cn.hutool.core.text.AntPathMatcher;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.config.NeOneConfigProperties;
import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 1R 认证拦截器
 *
 * @author fu yuan hui
 * @date 2023-08-08 21:34:44 Tuesday
 * <p>
 * 请求 ---> 网关 ---过滤器 ---拦截器(preHandle方法) --- AOP --- 全局异常
 * ^                                                           |
 * |                                                           |
 * —————————————————————拦截器(afterCompletion方法)<—————————————-
 * </p>
 */
@Slf4j
public class OneRecordServerAuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    private NeOneConfigProperties neOneConfigProperties;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 校验是否开启全局认证
        if (!this.neOneConfigProperties.isEnableAuth()) {
            return true;
        }
        // 白名单url放行
        String url = request.getRequestURI().replaceAll(request.getContextPath(), "");
        if (isIgnore(url)) {
            return true;
        }
        // 除了HandlerMethod，请求可能会被映射到静态资源处理器或者其他处理器上面，所以，除了controller接口处理器，其他处理器的情况需要放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 当HandlerMethod处理器上没有需要认证的注解时，也直接放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean annotationPresent = handlerMethod.getBeanType().isAnnotationPresent(Authenticated.class);
        if (!annotationPresent) {
            Method method = handlerMethod.getMethod();
            if (!AnnotatedElementUtils.hasAnnotation(method, Authenticated.class)) {
                return true;
            }
        }
        // 当从请求头获取到的TOKEN为空时，抛出401异常
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            throw new OneRecordServerException(401, "Not authenticated");
        }
        // 校验Token的合法性
        String accessToken = authorization.replaceAll(BaseConstants.DEFAULT_TOKEN_TYPE, "").trim();
        String[] clientInfo = verifyToken(accessToken);
        // 将client信息放入到ThreadLocal变量中
        AuthClientConfigThreadLocal.setClientId(clientInfo[0]);
        AuthClientConfigThreadLocal.setClientSecret(clientInfo[1]);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 移除ThreadLocal变量
        AuthClientConfigThreadLocal.clear();
    }

    /**
     * 判断是否是白名单中需要忽略的请求路径
     *
     * @param url url
     * @return 是白名单则返回true，不是则返回false
     */
    private boolean isIgnore(String url) {
        List<String> ignoreInterceptorUrls = this.neOneConfigProperties.getIgnoreInterceptorUrls();
        for (String ignoreInterceptorUrl : ignoreInterceptorUrls) {
            if (this.matcher.match(ignoreInterceptorUrl, url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验Token
     * 主要通过是否能解析出 clientId, client_secret两个字段来判断是否合法
     *
     * @param token token
     * @return clientId, client_secret
     */
    private String[] verifyToken(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC384(BaseConstants.JWT_SIGN_KEY)).build();
            DecodedJWT verify = jwtVerifier.verify(token);
            String clientId = verify.getClaim("client_id").asString();
            String clientSecret = verify.getClaim("client_secret").asString();
            if (StringUtils.isAnyBlank(clientId, clientSecret)) {
                throw new OneRecordServerException(401, "Not authenticated");
            }
            return new String[]{clientId, clientSecret};
        } catch (TokenExpiredException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>token过期：{}", e.getMessage());
            log.error(e.toString(), e);
            throw new OneRecordServerException(401, "Not authenticated");
        } catch (JWTVerificationException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>token验证失败：{}", e.getMessage());
            log.error(e.toString(), e);
            throw new OneRecordServerException(401, "Not authenticated");
        } catch (Throwable e) {
            log.error(">>>>>>>>>>>>>>>>>>>>验证token发生错误：{}", e.getMessage());
            log.error(e.toString(), e);
            throw new OneRecordServerException(401, "Not authenticated");
        }
    }
}
