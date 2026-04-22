package com.efreight.base.common.satoken.core;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.ObjectUtil;
import com.efreight.base.common.core.model.LoginUser;
import com.efreight.base.common.satoken.constant.UserType;
import com.efreight.base.common.satoken.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限管理实现类
 *
 * @author Lion Li
 */
@Slf4j
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("加载权限列表...");
        LoginUser loginUser = SecurityUtil.getUser();
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.ADMIN_LOGIN) {
            return ObjectUtil.defaultIfNull(loginUser.getPermissionList(), new ArrayList<>());
        } else {
            // 其他端 自行根据业务编写
        }
        return new ArrayList<>();
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("加载角色列表...");
        LoginUser loginUser = SecurityUtil.getUser();
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.ADMIN_LOGIN) {
            return ObjectUtil.defaultIfNull(loginUser.getRoles(), new ArrayList<>());
        } else {
            // 其他端 自行根据业务编写
        }
        return new ArrayList<>();
    }
}
