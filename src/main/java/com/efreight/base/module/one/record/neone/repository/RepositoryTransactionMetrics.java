// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RepositoryTransactionMetrics {

    private final Counter txBegin;

    private final Counter txCommit;

    private final Counter txRollback;

    public RepositoryTransactionMetrics(MeterRegistry registry) {
        this.txBegin = Counter.builder("rdf4j.repository.tx")
                .tag("action", "begin")
                .register(registry);
        this.txCommit = Counter.builder("rdf4j.repository.tx")
                .tag("action", "commit")
                .register(registry);
        this.txRollback = Counter.builder("rdf4j.repository.tx")
                .tag("action", "rollback")
                .register(registry);
    }

    public Counter txBegin() {
        return txBegin;
    }

    public Counter txCommit() {
        return txCommit;
    }

    public Counter txRollback() {
        return txRollback;
    }
}
