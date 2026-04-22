package com.efreight.base.common.core.model;

import cn.hutool.core.util.ObjectUtil;
import com.efreight.base.common.core.constant.CommonConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author alex
 * @date 2022/04/28
 */
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 企业id
     */
    private Long orgId;
    /**
     * 企业code
     */
    private String orgCode;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 帐号状态（0正常 1停用）
     */
    private Integer status;

    private Integer isAgent;

    private Long agentId;

    /**
     * 角色对象
     */
    private List<String> roles;
    /**
     * 企业组
     */
    private List<Long> orgIds;

    @ApiModelProperty("代理类型  0 托运 1 销售")
    private Integer agentType;

    @ApiModelProperty("白云物流航司代码")
    private String carrierCodeCan;

    @ApiModelProperty("代理名称")
    private String agentName;

    @ApiModelProperty("代理联系电话")
    private String agentTelephone;

    @ApiModelProperty("数据权限范围，0：本代理，1：所有代理，2：自定义代理范围")
    private Integer dataScope;

    @ApiModelProperty("自定义代理范围")
    private List<String> dataScopeAgentList;

    /**
     * 拥有的权限对象
     */
    private List<String> permissionList;

    public Integer getIsAgent() {
        return ObjectUtil.defaultIfNull(isAgent, CommonConstants.BOOLEAN_NO);
    }
}
