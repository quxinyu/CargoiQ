package com.efreight.base.module.one.record.neone.interceptor;

/**
 * @author fu yuan hui
 * @since 2024-11-29 15:09:11 星期五
 */
public final class AuthClientConfigThreadLocal {

    private static final ThreadLocal<String> CLIENT_ID_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<String> CLIENT_SECRET_HOLDER = new ThreadLocal<>();

    public static void setClientId(String clientId) {
        CLIENT_ID_HOLDER.set(clientId);
    }

    public static String getClientId() {
        return CLIENT_ID_HOLDER.get();
    }

    public static void setClientSecret(String clientSecret) {
        CLIENT_SECRET_HOLDER.set(clientSecret);
    }

    public static String getClientSecret() {
        return CLIENT_SECRET_HOLDER.get();
    }

    public static void clear() {
        CLIENT_ID_HOLDER.remove();
        CLIENT_SECRET_HOLDER.remove();
    }

}
