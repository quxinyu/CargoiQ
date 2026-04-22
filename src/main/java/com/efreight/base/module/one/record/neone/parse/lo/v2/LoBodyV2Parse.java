package com.efreight.base.module.one.record.neone.parse.lo.v2;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.efreight.base.common.core.model.NeOneResolvedData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fu yuan hui
 * @since 2024-08-21 14:33:30 星期三
 */
@Slf4j
public class LoBodyV2Parse {

    private static final List<LoBodyMessageV2Parser> PARSER_LIST = new ArrayList<>();

    static {
        PARSER_LIST.add(new FwbLoBodyMessageV2Parse());
        PARSER_LIST.add(new FhlLoBodyMessageV2Parse());
        PARSER_LIST.add(new FsuLoBodyMessageV2Parse());
    }

    public static NeOneResolvedData parse(String loBody) {
        JSONObject jsonObject = JSONObject.parseObject(loBody, Feature.DisableSpecialKeyDetect);
        for (LoBodyMessageV2Parser parser : PARSER_LIST) {
            if (parser.select(jsonObject)) {
                return parser.parse(jsonObject);
            }
        }

        log.error(">>>>>>>>>>>>>>>>>>>>>>[V2]无法从OneRecord Json Body 中识别出是哪种报文类型，OneRecord body ：{}", loBody);

        return null;
    }
}
