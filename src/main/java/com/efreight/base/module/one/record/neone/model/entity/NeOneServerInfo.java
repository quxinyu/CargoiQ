package com.efreight.base.module.one.record.neone.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author fu yuan hui
 * @since 2024-09-10 17:48:22 星期二
 */
@TableName("ne_one_server_info")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NeOneServerInfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uuid值
     */
    private String baseIri;

    /**
     * company url
     */
    private String companyIri;

    /**
     * company id
     */
    private String companyId;

    /**
     * 地址码，7位
     */
    private String serverAddressCode;

    /**
     * json
     */
    private String serverBody;


    /**
     * 区别是自己server，还是别人的server
     */
    private String serverFrom = "OTHER";

    /**
     * 0：表示不删除，其他表示删除
     */
    @TableLogic(value = "0", delval = "id")
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    public NeOneServerInfo(String baseIri, String companyId, String companyIri, String serverAddressCode, String serverBody, FromType fromType) {
        this.baseIri = baseIri;
        this.companyId = companyId;
        this.companyIri = companyIri;
        this.serverAddressCode = serverAddressCode;
        this.serverBody = serverBody;
        this.serverFrom = fromType.name();
    }

    public NeOneServerInfo(String baseIri, String companyId, String companyIri, String serverAddressCode, String serverBody,
                           FromType fromType, String callbackUrl, String subscribeUrl, String unsubscribeUrl, String tokenUrl, Integer tokenExp) {
        this.baseIri = baseIri;
        this.companyId = companyId;
        this.companyIri = companyIri;
        this.serverAddressCode = serverAddressCode;
        this.serverBody = serverBody;
        this.serverFrom = fromType.name();
    }

    public NeOneServerInfo(String baseIri, String companyId, String companyIri, String serverAddressCode, String serverBody) {
        this.baseIri = baseIri;
        this.companyId = companyId;
        this.companyIri = companyIri;
        this.serverAddressCode = serverAddressCode;
        this.serverBody = serverBody;
        this.serverFrom = "OTHER";
    }
}
