package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qiuguan
 * @date 2024/09/16 12:30:44  星期一
 */
@TableName("ne_one_company_holder")
@Data
public class NeOneServerCompanyHolder {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *  neone server host
     */
    private String host;

    /**
     * compnay 物流对象
     */
    private String companyIri;

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
     * 公司类别：比如航司，货站，代理等
     */
    private String category;

    /**
     * 公司地址
     */
    private String address;

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
     *  company json
     */
    private String companyBody;

    /**
     * 状态
     */
    private String companyStatus;

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
     * 外部企业，还是自身企业
     * "OTHER"
     * "SELF"
     */
    private String companyType = FromType.OTHER.name();

    /**
     * 0：表示不删除，其他表示删除
     */
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    public boolean matchHost(String iri) {
        String s1 = this.host.replaceAll("/", "");
        String s2 = iri.replaceAll("/", "");
        return s1.equals(s2);
    }

}
