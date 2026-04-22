// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.security;

import javax.enterprise.util.Nonbinding;
import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface AclSecured {

    /**
     * The access mode needed to call the method.
     * <p>
     * There must be exactly <b>one</b> {@link AccessMode} in the array.
     * <p>
     * This annotation works with {@link AclInterceptor} to enforce ACL-based
     * access control. The interceptor will check if the current user has the
     * specified permission on the logistics object identified by parameters
     * annotated with {@link AccessObject}.
     * <p>
     * Example usage:
     * <pre>
     * &#64;AclSecured({
     *     &#64;AccessMode(value = AccessMode.READ)
     * })
     * public LogisticsObject getLogisticsObject(
     *         &#64;AccessObject(AccessObject.Type.LOGISTICS_OBJECT_ID)
     *         String loId) {
     *     // method implementation
     * }
     * </pre>
     *
     * @return the {@link AccessMode} required to access the method.
     */
    @Nonbinding AccessMode[] value() default {};
}
