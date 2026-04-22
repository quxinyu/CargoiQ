// Copyright Open Logistics Foundation
//
// Licensed under the Open Logistics Foundation License 1.3.
// For details on the licensing terms, see the LICENSE file.
// SPDX-License-Identifier: OLFL-1.3

package com.efreight.base.module.one.record.neone.repository;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class RepositoryTransaction {

    private static final Logger log = LoggerFactory.getLogger(RepositoryTransaction.class);

    private final Repository repository;

    private final RepositoryTransactionMetrics metrics;

    private final Executor executor;

    public RepositoryTransaction(@Qualifier("rdf4jRepository") Repository repository,
                                 RepositoryTransactionMetrics metrics,
                                 @Qualifier("transactionHookExecutor") Executor executor) {
        this.repository = repository;
        this.metrics = metrics;
        this.executor = executor;
    }

    public void transactionallyDo(Consumer<RepositoryConnection> unitOfWork) {
        transactionallyGet(((connection, repositoryTransactionHooks) -> {
            unitOfWork.accept(connection);
            return null;
        }));
    }

    public void transactionallyDo(BiConsumer<RepositoryConnection, RepositoryTransactionHooks> unitOfWork) {
        transactionallyGet((connection, repositoryTransactionHooks) -> {
            unitOfWork.accept(connection, repositoryTransactionHooks);
            return null;
        });
    }

    public <T> T transactionallyGet(Function<RepositoryConnection, T> unitOfWork) {
        return transactionallyGet((connection, repositoryTransactionHooks) -> unitOfWork.apply(connection));
    }

    public <T> T transactionallyGet(BiFunction<RepositoryConnection, RepositoryTransactionHooks, T> unitOfWork) {
        final String txid = NanoIdUtils.randomNanoId();
        try (RepositoryConnection connection = repository.getConnection()) {
            log.debug("Connection opened [{}]", txid);
            RepositoryTransactionHooks hooks = new RepositoryTransactionHooks(executor);
            log.debug("Starting transaction [{}]", txid);
            connection.begin();
            metrics.txBegin().increment();
            T result;
            try {
                result = unitOfWork.apply(connection, hooks);
                log.debug("Committing transaction [{}]", txid);
                long start = System.currentTimeMillis();
                connection.commit();
                long duration = System.currentTimeMillis() - start;
                log.debug("Committed [{}] in [{}] ms", txid, duration);
                hooks.runPostCommitHooks();
                metrics.txCommit().increment();
                return result;
            } catch (RuntimeException exception) { // for every runtime exception the transaction is rolled back
                log.warn("Exception during transaction, rolling back transaction, [{}}", txid, exception);
                connection.rollback();
                hooks.runPostRollbackHooks();
                metrics.txRollback().increment();
                throw exception;
            }
        } finally {
            log.debug("Connection close [{}]", txid);
        }
    }

    public static class RepositoryTransactionHooks {

        private static final Logger log = LoggerFactory.getLogger(RepositoryTransactionHooks.class);

        private final List<Runnable> postCommitHooks;

        private final List<Runnable> postRollbackHooks;

        private final Executor executor;

        public RepositoryTransactionHooks(Executor executor) {
            this.executor = executor;
            this.postCommitHooks = new ArrayList<>();
            this.postRollbackHooks = new ArrayList<>();
        }

        public void registerPostCommitHook(Runnable hook) {
            this.postCommitHooks.add(hook);
        }

        public void registerPostRollbackHook(Runnable hook) {
            this.postRollbackHooks.add(hook);
        }

        public void runPostCommitHooks() {
            runHooks(this.postCommitHooks, "POST_COMMIT");
        }

        public void runPostRollbackHooks() {
            runHooks(this.postRollbackHooks, "POST_ROLLBACK");
        }

        private void runHooks(List<Runnable> hooks, String phase) {
            try {
                hooks.forEach(executor::execute);
            } catch (RuntimeException exception) {
                log.error("Exception running [{}] hook", phase, exception);
            }
        }
    }

}
