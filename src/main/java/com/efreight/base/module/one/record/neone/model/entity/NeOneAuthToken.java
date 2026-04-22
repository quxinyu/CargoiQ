package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 */
@Data
@TableName("ne_one_auth_token")
public class NeOneAuthToken {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonProperty("orgName")
    @TableField("org_name")
    private String orgName;

    @JsonProperty("parentOrgName")
    @TableField("parent_org_name")
    private String parentOrgName;

    @JsonProperty("clientId")
    @TableField("client_id")
    private String clientId;

    @JsonProperty("clientSecret")
    @TableField("client_secret")
    private String clientSecret;

    @JsonProperty("grantType")
    @TableField("grant_type")
    private String grantType;


    @JsonProperty("username")
    @TableField("user_name")
    private String username;

    @JsonProperty("password")
    @TableField("password")
    private String password;

    @JsonProperty("opposite")
    @TableField("opposite")
    private String opposite;

    @JsonProperty("exp")
    @TableField("exp")
    private Integer exp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("createTime")
    @TableField("create_time")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updateTime")
    @TableField("update_time")
    private LocalDateTime updateTime;
}