package com.efreight.base.common.satoken.util;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.efreight.base.common.core.model.LoginUser;
import com.efreight.base.common.satoken.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * @author alex
 * @date 2022/04/28
 */
@Slf4j
public class SecurityUtil {

    public static LoginUser getUser() {
        SaSession session = SessionUtil.getSession();
        if (session != null) {
            return (LoginUser) session.get(SecurityConstants.LOGIN_USER_KEY);
        }
        return null;
    }

    /**
     * 清除用户信息缓存
     *
     * @param account
     */
    public static void clearUserCache(String account) {
        SaSession session = SessionUtil.getSessionByLoginId(account);
        if (session != null) {
            session.clear();
        }
    }

    /**
     * 踢用户下线
     *
     * @param account
     */
    public static void kickOutUser(String account) {
        StpUtil.kickout(account);
    }

    public static void refreshLoginUser(LoginUser loginUser) {
        SaSession session = SessionUtil.getSession();
        if (session != null) {
            session.set(SecurityConstants.LOGIN_USER_KEY, loginUser);
        }
    }
}
