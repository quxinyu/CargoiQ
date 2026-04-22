// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.security;

import org.eclipse.rdf4j.model.IRI;
import org.springframework.stereotype.Component;

/**
 * 内部访问主体
 * <p>
 * 包装 AccessSubject，提供对访问者的抽象
 * <p>
 * 当没有认证用户时，isResolvable() 返回 false
 * <p>
 * 默认情况下，如果没有配置 AccessSubject Bean，则使用内部默认的 IRI
 *
 * @author test
 * @since 2025-01-09
 */
@Component
public class InternalAccessSubject {

    private final AccessSubject accessSubject;

    /**
     * 构造函数
     * <p>
     * AccessSubject 是可选的依赖，如果没有提供，则使用内部系统用户
     *
     * @param accessSubject 访问主体（可选）
     */
    public InternalAccessSubject(
            @org.springframework.beans.factory.annotation.Autowired(required = false)
            AccessSubject accessSubject) {
        // 如果没有提供 AccessSubject，创建一个默认的内部系统用户
        if (accessSubject == null) {
            // 使用内部系统 IRI - rdf4j 3.7.4 兼容方式
            org.eclipse.rdf4j.model.impl.SimpleValueFactory factory =
                    org.eclipse.rdf4j.model.impl.SimpleValueFactory.getInstance();
            IRI internalIri = factory.createIRI("urn:internal:system");
            this.accessSubject = AccessSubject.from(internalIri);
        } else {
            this.accessSubject = accessSubject;
        }
    }

    /**
     * 获取访问者的 IRI
     *
     * @return 访问者的 IRI
     */
    public IRI iri() {
        return accessSubject.iri();
    }

    /**
     * 检查是否有可解析的访问主体
     *
     * @return 始终返回 true（至少有默认的内部系统用户）
     */
    public boolean isResolvable() {
        return true;  // 总是可解析，因为至少有默认的内部系统用户
    }
}
