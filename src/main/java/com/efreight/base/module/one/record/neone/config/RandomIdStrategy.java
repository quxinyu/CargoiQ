// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.config;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.util.function.Supplier;

public enum RandomIdStrategy {

    UUID(() -> java.util.UUID.randomUUID().toString()),
    NANOID(NanoIdUtils::randomNanoId);

    private final Supplier<String> randomId;


    RandomIdStrategy(Supplier<String> randomId) {
        this.randomId = randomId;
    }

    public String randomId() {
        return randomId.get();
    }
}
