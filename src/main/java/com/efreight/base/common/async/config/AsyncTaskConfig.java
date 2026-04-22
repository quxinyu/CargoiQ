package com.efreight.base.common.async.config;

import com.efreight.base.common.async.config.properties.AsyncProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步任务配置
 *
 * @author zhuangyan
 * Date: 2022/09/21
 */
@Slf4j
@Setter
@Getter
@EnableAsync
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({AsyncProperties.class})
public class AsyncTaskConfig extends AsyncConfigurerSupport {

    private final AsyncProperties asyncProperties;

    /**
     * 通用线程池配置
     * 按照properties中的配置设置的线程池，用于通常的异步任务，线程可以感知上下文信息
     *
     * @return Executor
     */
    @Override
    @Bean(name = "simpleAsyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());
        executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 增加 TaskDecorator 属性的配置
        executor.setTaskDecorator(new ContextDecorator());
        log.info("==========> 创建异步线程池 simpleAsyncExecutor, corePoolSize = {}, maxPoolSize = {}, queueCapacity = {}, keepAliveSeconds = {}", asyncProperties.getCorePoolSize(), asyncProperties.getMaxPoolSize(), asyncProperties.getQueueCapacity(), asyncProperties.getKeepAliveSeconds());
        return executor;
    }

    /**
     * 专用于处理MNS任务的线程池配置
     *
     * @return Executor
     */
    @Bean(name = "mnsTaskExecutor")
    public ThreadPoolTaskExecutor mnsTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = Runtime.getRuntime().availableProcessors() << 1;
        int maxPoolSize = Runtime.getRuntime().availableProcessors() << 2;
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("mns-consume-task:");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        log.info("==========> 创建异步线程池 mnsTaskExecutor, corePoolSize = {}, maxPoolSize = {}, queueCapacity = {}, keepAliveSeconds = {}", corePoolSize, maxPoolSize, 1000, 30);
        return executor;
    }


    /**
     * generalTaskExecutor
     *
     * @return Executor
     */
    @Bean(name = "generalTaskExecutor")
    public ThreadPoolTaskExecutor generalTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(64);
        executor.setQueueCapacity(1024);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("general-async-task:");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        log.info("==========> 创建异步线程池 mnsTaskExecutor, corePoolSize = {}, maxPoolSize = {}, queueCapacity = {}, keepAliveSeconds = {}", 16, 64, 1024, 30);
        return executor;
    }

    /**
     * 专用于处理FSU任务的线程池配置
     *
     * @return Executor
     */
    @Bean(name = "fsuTaskExecutor")
    public ThreadPoolTaskExecutor fsuTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int corePoolSize = Runtime.getRuntime().availableProcessors() << 1;
        int maxPoolSize = Runtime.getRuntime().availableProcessors() << 2;
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("fsu-process-task:");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        log.info("==========> 创建异步线程池 mnsTaskExecutor, corePoolSize = {}, maxPoolSize = {}, queueCapacity = {}, keepAliveSeconds = {}", corePoolSize, maxPoolSize, 1000, 30);
        return executor;
    }


    /**
     * 专用于处理流水线任务的线程池配置
     *
     * @return Executor
     */
    @Bean(name = "pipelineTriggerExecutor")
    public ThreadPoolTaskExecutor pipelineTriggerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(32);
        executor.setMaxPoolSize(128);
        executor.setQueueCapacity(5000);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("studio-pipeline-trigger-async:");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        log.info("==========> 创建异步线程池 mnsTaskExecutor, corePoolSize = {}, maxPoolSize = {}, queueCapacity = {}, keepAliveSeconds = {}", 32, 128, 5000, 30);
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    /**
     * 异步任务装饰器
     */
    private static final class ContextDecorator implements TaskDecorator {

        /**
         * 装饰异步任务
         *
         * @param runnable 要执行的任务
         * @return Runnable
         */
        @Override
        public Runnable decorate(Runnable runnable) {
            RequestAttributes context = RequestContextHolder.currentRequestAttributes();
            return () -> {
                try {
                    // 传递上下文
                    RequestContextHolder.setRequestAttributes(context);
                    runnable.run();
                } finally {
                    // 执行结束清理上下文信息，避免线程池复用线程导致的上下文残留
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        }
    }
}
