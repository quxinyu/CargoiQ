package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.common.core.exception.OneRecordServerException;
import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.config.NeOneConfigProperties;
import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.enums.FromType;
import com.efreight.base.module.one.record.neone.ex.LogisticsObjectException;
import com.efreight.base.module.one.record.neone.helper.IriGenerator;
import com.efreight.base.module.one.record.neone.model.entity.NeOneLogisticsObjects;
import com.efreight.base.module.one.record.neone.service.NeOneLogisticsObjectsService;
import com.efreight.base.module.one.record.neone.service.NeOneServerInfoService;
import com.efreight.base.module.one.record.neone.utils.ResponseEntityBuilder;
import com.efreight.base.module.one.record.neone.utils.UUIDTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author fu yuan hui
 * @since 2024-08-14 13:59:04 星期三
 */
@Slf4j
@Authenticated
@RestController
@RequiredArgsConstructor
@RequestMapping("/logistics-objects")
public class NeOneLogisticsObjectsController {

    private String companyObject;

    private final IriGenerator iriGenerator;

    private final NeOneLogisticsObjectsService logisticsObjectsService;

    private final NeOneConfigProperties neOneConfigProperties;

    private final NeOneServerInfoService neOneServerInfoService;

    /**
     * 客户端可以传递的content-Type 限制为：application/json 和 application/ld+json
     */
    @PostMapping(consumes = {BaseConstants.CONTENT_TYPE_LD_JSON, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createLogisticsObject(@RequestBody String oneRecordBody) {

        String uuid = UUIDTools.generateSimpleUUID();
        String iri = iriGenerator.generateLogisticsObjectLoId(uuid);

        logisticsObjectsService.createResolveBody(iri, oneRecordBody, FromType.CREATE_SELF);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, iri);
        return ResponseEntity
                .status(201)
                .headers(headers)
                .build();
    }

    /**
     * 更新单个lo对象
     *
     * @param loId          lo对象id
     * @param oneRecordUpdateBody lo对象内容
     * @return 返回信息
     */
    @PatchMapping(value = "/{loId}", consumes = {BaseConstants.CONTENT_TYPE_LD_JSON, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateLogisticsObject(@PathVariable String loId, @RequestBody String oneRecordUpdateBody) {

        String actionRequestIri = logisticsObjectsService.updateLogisticsObject(loId, oneRecordUpdateBody);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, actionRequestIri);
        headers.add("Type", "https://onerecord.iata.org/ns/api#ChangeRequest");
        return ResponseEntity
                .status(201)
                .headers(headers)
                .build();
    }

    /**
     * 更新单个lo对象
     *
     * @param loId          lo对象id
     * @return 返回信息
     */
    @GetMapping(value = "/{loId}/audit-trail", produces = {BaseConstants.CONTENT_TYPE_LD_JSON, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAuditTrial(@PathVariable String loId,
                                           @RequestParam(value = "updated-from", required = false) String updateFrom,
                                           @RequestParam(value = "updated-to", required = false) String updateTo,
                                           @RequestParam(value = "status", required = false) String status ) {

        String body = logisticsObjectsService.auditTrail(loId, updateFrom, updateTo, status);

        return ResponseEntity
                .status(200)
                .body(body);
    }


    /**
     * 增加 produces = "application/ld+json" 让其以JSON的结构返回出去
     */
    @SuppressWarnings("all")
    @GetMapping(value = "{id}", produces = BaseConstants.CONTENT_TYPE_LD_JSON)
    public ResponseEntity<?> getLogisticsObject(@PathVariable String id) {
        NeOneLogisticsObjects info = this.logisticsObjectsService.getWithLoId(id);
        if (info == null) {
            throw new LogisticsObjectException("Logistics Object not found", 404);
        }

        return ResponseEntityBuilder.ok()
                .header("Location", info.getIri())
                .header("Type", "https://onerecord.iata.org/ns/cargo#Company")
                .header("Revision", "1")
                .header("Latest-Revision", "1")
                .body(info.getBodyText());
    }


    @PostConstruct
    public void init() {
        try {
            this.companyObject = IOUtils.resourceToString("onerecord/company.json", StandardCharsets.UTF_8, NeOneLogisticsObjectsController.class.getClassLoader());
        } catch (IOException e) {
            throw new OneRecordServerException("JSON 文件初始化失败", e);
        }
    }
}
