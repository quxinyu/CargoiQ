//  Copyright Open Logistics Foundation
//
//  Licensed under the Open Logistics Foundation License 1.3.
//  For details on the licensing terms, see the LICENSE file.
//  SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.exception;

/**
 * Generic, unspecific server exception that results in HTTP 500 error code.
 */
public class NeoneException extends RuntimeException {

    public NeoneException(String message, Exception e) {
        super(message, e);
    }

    public NeoneException(String message) {
        super(message);
    }
}
