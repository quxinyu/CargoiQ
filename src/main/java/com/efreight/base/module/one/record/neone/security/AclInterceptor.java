// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.security;

import com.efreight.base.module.one.record.neone.exception.AccessDeniedException;
import com.efreight.base.module.one.record.neone.exception.NeoneException;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.service.onerecord.AclAuthorizationService;
import com.efreight.base.module.one.record.neone.service.onerecord.LogisticsObjectService;
import com.efreight.base.module.one.record.neone.service.onerecord.NeoneId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * ACL 权限拦截器
 * <p>
 * 使用 Spring AOP 实现，对带有 {@link AclSecured} 注解的方法进行权限拦截
 * <p>
 * 拦截器会检查方法参数中带有 {@link AccessObject} 注解的参数，根据注解指定的类型
 * 获取对应的物流对象 ID，然后调用 {@link AclAuthorizationService} 检查当前用户是否具有
 * 方法上 {@link AclSecured} 注解指定的访问权限
 *
 * @author test
 * @since 2025-01-16
 */
@Aspect
@Component
public class AclInterceptor {

    private final LogisticsObjectService loService;

    private final AclAuthorizationService aclService;

    private final InternalAccessSubject accessSubject;

    private final IdProvider idProvider;

    /**
     * 构造函数
     *
     * @param loService     物流对象服务
     * @param aclService    ACL 授权服务
     * @param accessSubject 内部访问主体
     * @param idProvider    ID 提供器
     */
    public AclInterceptor(LogisticsObjectService loService,
                          AclAuthorizationService aclService,
                          InternalAccessSubject accessSubject,
                          IdProvider idProvider) {
        this.loService = loService;
        this.aclService = aclService;
        this.accessSubject = accessSubject;
        this.idProvider = idProvider;
    }

    /**
     * 拦截带有 {@link AclSecured} 注解的方法
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 执行异常
     */
    @Around("@annotation(com.efreight.base.module.one.record.neone.security.AclSecured)")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<AccessObject.Type, String> typeValueMapping = getMethodParameterValueMapping(joinPoint);
        validateAccessObjectDeclaration(typeValueMapping);
        IRI accessObject = getAccessObjectId(typeValueMapping).getIri();
        IRI requiredMode = determineRequiredPermission(joinPoint);
        IRI agent = accessSubject.iri();
        checkPermission(agent, accessObject, requiredMode);
        return joinPoint.proceed();
    }

    /**
     * 根据类型值映射获取访问对象 ID
     *
     * @param typeValueMapping 类型和值的映射
     * @return NeoneId 访问对象 ID
     */
    private NeoneId getAccessObjectId(Map<AccessObject.Type, String> typeValueMapping) {
        NeoneId accessObjectId;
        switch (typeValueMapping.size()) {
            case 1:
                if (typeValueMapping.containsKey(AccessObject.Type.ACTION_REQUEST)) {
                    accessObjectId = idProvider.getActionRequestId(
                            typeValueMapping.get(AccessObject.Type.ACTION_REQUEST)
                    );
                } else {
                    accessObjectId = idProvider.getUriForLoId(
                            typeValueMapping.get(AccessObject.Type.LOGISTICS_OBJECT_ID)
                    );
                }
                break;
            case 2:
                accessObjectId = idProvider.getUriForEventId(
                        typeValueMapping.get(AccessObject.Type.LOGISTICS_OBJECT_ID),
                        typeValueMapping.get(AccessObject.Type.LOGISTICS_EVENT_ID)
                );
                break;
            default:
                throw new NeoneException("Invalid AccessObject definition");
        }

        return accessObjectId;
    }

    /**
     * 验证访问对象声明是否有效
     *
     * @param typeValueMapping 类型和值的映射
     */
    private void validateAccessObjectDeclaration(Map<AccessObject.Type, String> typeValueMapping) {
        boolean valid = false;

        switch (typeValueMapping.size()) {
            case 1:
                // if only one AccessObject.Type is present it must be either LOGISTICS_OBJECT_ID
                // or ACTION_REQUEST
                valid = typeValueMapping.containsKey(AccessObject.Type.LOGISTICS_OBJECT_ID) ||
                        typeValueMapping.containsKey(AccessObject.Type.ACTION_REQUEST);
                break;
            case 2:
                // if two AccessObject.Types are present it must be LOGISTICS_OBJECT_ID and
                // LOGISTICS_EVENT_ID
                valid = typeValueMapping.keySet().containsAll(
                        EnumSet.of(AccessObject.Type.LOGISTICS_OBJECT_ID, AccessObject.Type.LOGISTICS_EVENT_ID)
                );
                break;
            default:
                // 0 or more than 2 is invalid
                break;
        }

        if (!valid) {
            throw new NeoneException(
                    "Ambiguous access objects found, either exactly one AccessObject has to be " +
                            "defined and pointing to a LogisticsObject or an ActionRequest internal id, " +
                            "or two AccessObjects have to be defined, one pointing to a LogisticsObject internal id " +
                            "and the other pointing to a LogisticsEvent internal id."
            );
        }
    }

    /**
     * 获取方法参数中带有 {@link AccessObject} 注解的参数值映射
     *
     * @param joinPoint 连接点
     * @return 类型到参数值的映射
     */
    private static Map<AccessObject.Type, String> getMethodParameterValueMapping(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] methodParams = method.getParameters();
        Object[] args = joinPoint.getArgs();
        Map<AccessObject.Type, String> typeValueMapping = new HashMap<>();
        for (int i = 0; i < methodParams.length; i++) {
            AccessObject accessObject = methodParams[i].getAnnotation(AccessObject.class);
            if (accessObject != null) {
                if (!(args[i] instanceof String)) {
                    throw new NeoneException(
                            String.format("参数 [%s] 上的 AccessObject 声明无效，必须是 String 类型，实际为 [%s]",
                                    methodParams[i].getName(),
                                    methodParams[i].getType().getName()
                            )
                    );
                }
                typeValueMapping.put(accessObject.value(), (String) args[i]);
            }
        }
        return typeValueMapping;
    }

    /**
     * 确定方法所需的权限
     *
     * @param joinPoint 连接点
     * @return 权限 IRI
     */
    private IRI determineRequiredPermission(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AclSecured aclSecured = method.getAnnotation(AclSecured.class);

        if (aclSecured == null) {
            throw new NeoneException(
                    String.format(
                            "No access mode declared on [%s] in class [%s]",
                            method.getName(),
                            method.getDeclaringClass().getName()
                    )
            );
        }

        if (aclSecured.value().length != 1) {
            throw new NeoneException(
                    String.format(
                            "Invalid access mode declaration [%s] in class [%s]. " +
                                    "There needs to be exactly one declared access mode.",
                            method.getName(),
                            method.getDeclaringClass().getName()
                    )
            );
        }

        return Values.iri(aclSecured.value()[0].value());
    }

    /**
     * 检查权限
     *
     * @param agent    代理 IRI
     * @param accessTo 访问对象 IRI
     * @param mode     访问模式 IRI
     */
    private void checkPermission(IRI agent, IRI accessTo, IRI mode) {
        if (!aclService.isAuthorized(agent, accessTo, mode)) {
            throw new AccessDeniedException(agent, accessTo);
        }
    }
}
