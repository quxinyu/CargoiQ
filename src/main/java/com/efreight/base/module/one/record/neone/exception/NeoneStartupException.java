// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.exception;

public class NeoneStartupException extends RuntimeException {

    public NeoneStartupException(String message) {
        super(message);
    }

    public NeoneStartupException(Throwable cause) {
        super(cause);
    }

    public NeoneStartupException(String message, Throwable cause) {
        super(message, cause);
    }
}
