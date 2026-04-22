package com.efreight.base.module.one.record.neone.parse.lo.v3;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.common.core.model.NeOneResolvedData;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fu yuan hui
 * @since 2024-08-21 14:33:30 星期三
 *
 *
 * FWB, FHL, FFR, FSU 这4种报文解析都是基于向霞提供的结构进行解析的，我也和官方的进行了对比，还是有差异的。所以
 * 如果是官方的报文体这可能就会有问题
 */
@Slf4j
public class LoBodyV3Parse {

    private static final List<LoBodyMessageParser> PARSER_LIST = new ArrayList<>();

    static {
        PARSER_LIST.add(new FwbLoBodyMessageParse());
        PARSER_LIST.add(new FhlLoBodyMessageParse());
        PARSER_LIST.add(new FsuLoBodyMessageParse());
        PARSER_LIST.add(new FFRLoBodyMessageParse());
    }

    public static NeOneResolvedData parse(String loBody) {
        for (LoBodyMessageParser parser : PARSER_LIST) {
            if (parser.select(loBody)) {
                return parser.parse(loBody);
            }
        }

        log.error(">>>>>>>>>>>>>>>>>>>>>>[V3]无法从OneRecord Json Body 中识别出是哪种报文类型，OneRecord body ：{}", loBody);

        throw new OneRecordServerException("报文解析错误");
    }
}
