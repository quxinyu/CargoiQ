package com.efreight.base.common.satoken.util;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;

/**
 * @author alex
 * @date 2023/07/31
 */
public class SessionUtil {

    public static SaSession getSession() {
        try {
            return StpUtil.getSession(false);
        } catch (Exception e) {
            return null;
        }
    }

    public static SaSession getSessionByLoginId(Object loginId) {
        try {
            return StpUtil.getSessionByLoginId(loginId, false);
        } catch (Exception e) {
            return null;
        }
    }
}
