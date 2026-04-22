package com.efreight.base.common.poi.handler;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.naming.NamingService;
import com.efreight.base.common.core.utils.StringUtils;
import com.efreight.base.common.poi.annotation.Dict;
import com.efreight.base.common.poi.api.AdminApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * 自定义数据字典拦截处理
 *
 * @author alex
 * @date 2022/08/12
 */
@Slf4j
public class DictCellWriteHandler implements CellWriteHandler {

    /**
     * 字典项缓存，避免频繁去redis读
     */
    private Map<String, JSONArray> CACHE_DICT_MAP = new HashMap<>();

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        if (context.getHead()) {
            return;
        }
        if (context.getHeadData() == null) {
            return;
        }
        Field field = context.getHeadData().getField();
        if (field == null) {
            return;
        }
        Dict dict = field.getAnnotation(Dict.class);
        Cell cell = context.getCell();
        if (dict != null) {
            String value = null;
            switch (cell.getCellType()) {
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    value = cell.getNumericCellValue() + "";
                    break;
            }

            if (StringUtils.isBlank(value)) {
                return;
            }

            cell.setCellValue(getDictItemText(value, getDictItemList(dict.value())));
        }
    }

    @NacosInjected
    private NamingService namingService;

    /**
     * 通过字典code获取字典项列表
     *
     * @param dictCode
     * @return
     */
    private JSONArray getDictItemList(String dictCode) {
        if (this.CACHE_DICT_MAP.containsKey(dictCode)) {
            return this.CACHE_DICT_MAP.get(dictCode);
        }

        JSONArray list = AdminApi.getDictByType(dictCode);
        this.CACHE_DICT_MAP.put(dictCode, list);

        return list;
    }

    /**
     * 通过字典项值获取字典项名
     *
     * @param dictItemValue
     * @param dictItemList
     * @return
     */
    private String getDictItemText(String dictItemValue, JSONArray dictItemList) {
        if (CollectionUtils.isEmpty(dictItemList)) {
            return null;
        }
        for (int i = 0; i < dictItemList.size(); i++) {
            JSONObject json = dictItemList.getJSONObject(i);
            if (dictItemValue.equals(json.getString("dictValue"))) {
                return json.getString("dictLabel");
            }
        }
        return null;
    }
}
