package com.efreight.base.module.one.record.neone.parse.lo.v3;

import com.alibaba.fastjson.JSONObject;
import com.efreight.base.common.core.model.NeOneResolvedData;

/**
 * @author fu yuan hui
 * @since 2024-08-21 15:17:24 星期三
 */
public interface LoBodyMessageParser {

    boolean select(String type);

    NeOneResolvedData parse(String body);
}
