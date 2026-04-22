package com.efreight.base.module.one.record.neone.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qiuguan
 * @date 2024/09/16 12:30:44  星期一
 */
@Data
public class CompanyHolderVo implements Serializable {

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
    @JsonIgnore
    private String companyBody;

    /**
     * 状态
     */
    private String companyStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
