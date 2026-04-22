package com.efreight.base.module.one.record.neone.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qiuguan
 * @date 2024/09/17 09:51:34  星期二
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyHolderRequest extends BaseParam implements Serializable {

    private Long id;

    /**
     * neone server host
     */
    private String host;

    /**
     * company id
     */
    private String companyId;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 公司简称
     */
    private String companyShortName;

    /**
     * 地址
     */
    private String address;

    /**
     * 公司角色，比如航司，货站，代理等等
     */
    private String category;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系人昵称
     */
    private String contactShortName;

    /**
     * 称呼
     */
    private String salutation;

    /**
     * 联系方式
     */
    private String contactMethod;

    /**
     * 状态
     */
    private String companyStatus;

    /**
     * 地址码
     */
    private String serverAddressCode;

    /**
     * neone回调地址
     */
    private String callbackUrl;

    /**
     * neone订阅地址
     */
    private String subscribeUrl;

    /**
     * neone取消订阅地址
     */
    private String unsubscribeUrl;

    /**
     * neone获取token
     */
    private String tokenUrl;

    /**
     * neone token有效期
     */
    private Integer tokenExp;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
