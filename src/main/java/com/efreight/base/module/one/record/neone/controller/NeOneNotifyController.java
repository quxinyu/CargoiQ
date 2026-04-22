package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.service.NeOneNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接收通知
 *
 * @author fu yuan hui
 * @since 2024-09-09 10:51:32 星期一
 */
@Slf4j
@Authenticated
@RestController
@RequiredArgsConstructor
public class NeOneNotifyController {

    private final NeOneNotifyService notifyService;

    /**
     * 接收外部系统通知
     *
     * @param notifyBody 通知体
     * @return 204 no content
     */
    @PostMapping(value = "notifications", consumes = {"application/ld+json", MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> notifications(@RequestBody String notifyBody) {
        log.info(">>>>>>>>>>>>>>>>>>.收到通知： {}", notifyBody);
        this.notifyService.receiveNotify(notifyBody);
        return ResponseEntity.status(204).build();
    }
}
