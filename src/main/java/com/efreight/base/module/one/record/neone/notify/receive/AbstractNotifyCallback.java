package com.efreight.base.module.one.record.neone.notify.receive;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.function.Supplier;

/**
 * 通知回调
 *
 * @author fu yuan hui
 * @since 2024-09-14 13:26:56 星期六
 */
public abstract class AbstractNotifyCallback<T> implements NotifyCallback<T>, InitializingBean {

    protected static final Configuration CONF = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    @Autowired
    protected ThreadPoolTaskExecutor notifyExecutor;

    @Override
    public void callback(Supplier<T> supplier) {
        T t = supplier.get();
        this.notifyExecutor.execute(() -> {
            doCallback(t);
        });
    }

    protected abstract void doCallback(T t);

    protected void init() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
