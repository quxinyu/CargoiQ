package com.efreight.base.module.one.record.neone.resolve;

import com.efreight.base.module.one.record.neone.annotations.HandlerMethodArgumentAlias;
import com.efreight.base.module.one.record.neone.model.objects.AuthRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

/**
 * @author fu yuan hui
 */
public class HandlerMethodArgumentAliasResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(AuthRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AuthRequest clientRequest = new AuthRequest();
        Map<String, String[]> parameterMap = webRequest.getParameterMap();

        for (Field field : AuthRequest.class.getDeclaredFields()) {
            HandlerMethodArgumentAlias alias = field.getAnnotation(HandlerMethodArgumentAlias.class);
            String value = null;
            if (alias != null) {
                String[] aliases = alias.value();
                String defaultValue = alias.defaultValue();
                value = Arrays.stream(aliases)
                        .map(parameterMap::get)
                        .filter(arr -> arr != null && arr.length > 0)
                        .map(arr -> arr[0])
                        .findFirst()
                        .orElse(defaultValue);
            } else {
                String fieldName = field.getName();
                String[] values = parameterMap.get(fieldName);
                if (values != null && values.length > 0) {
                    value = values[0];
                }
            }

            if (value != null) {
                field.setAccessible(true);
                field.set(clientRequest, value);
            }
        }

        return clientRequest;
    }
}
