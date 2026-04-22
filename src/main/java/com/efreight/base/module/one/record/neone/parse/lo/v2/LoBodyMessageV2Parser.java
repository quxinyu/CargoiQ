package com.efreight.base.module.one.record.neone.parse.lo.v2;


import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.model.NeOneResolvedData;

/**
 * @author fu yuan hui
 * @since 2024-08-21 15:17:24 星期三
 */
public interface LoBodyMessageV2Parser {

    boolean select(JSONObject select);

    NeOneResolvedData parse(JSONObject jsonObject);
}
