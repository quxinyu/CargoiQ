
package com.efreight.base.common.core.constant;

/**
 * @author wcf
 */
public interface CommonConstants {

    /**
     * 否
     */
    int BOOLEAN_NO = 0;
    /**
     * 是
     */
    int BOOLEAN_YES = 1;
    /**
     * 删除标识：正常
     */
    int DEL_FLAG_NORMAL = 0;
    /**
     * 删除标识：已删除
     */
    int DEL_FLAG_DELETED = 1;

    /**
     * 删除
     */
    String STATUS_DEL = "1";
    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 菜单
     */
    String MENU = "0";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";

    /**
     * 成功标记
     */
    Integer SUCCESS = 0;
    /**
     * 成功信息
     */
    String SUCCESS_MSG = "操作成功";
    /**
     * 失败标记
     */
    Integer FAIL = 1;
    /**
     * 失败信息
     */
    String FAIL_MSG = "操作失败";
    /**
     * 用于链路追踪
     */
    String TRACE_ID = "trace-id";

    /**
     * 操作类型 save：新增 delete：删除 update：修改
     */
    interface LogType {
        String SAVE = "save";
        String DELETE = "delete";
        String UPDATE = "update";
    }

    /**
     * 模块 da 国内进港
     */
    interface Module {
        String DA = "da";
    }

    /**
     * 通用数据权限字段
     */
    interface DataScope {
        String CONSIGNEE_ORG_ID = "consignee_org_id";

        String LOGIC_AND = " AND ";

        String LOGIC_OR = " OR ";
    }
}
