package com.efreight.base.module.one.record.neone.controller;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.ex.ActionRequestException;
import com.efreight.base.module.one.record.neone.model.entity.NeOneActionRequests;
import com.efreight.base.module.one.record.neone.service.ActionRequestService;
import com.efreight.base.module.one.record.neone.utils.ResponseEntityBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author fu yuan hui
 * @since 2024-09-09 14:36:15 星期一
 */
@Slf4j
@Authenticated
@RestController
@RequiredArgsConstructor
@RequestMapping("/action-requests")
public class NeOneActionRequestController {


    private final ActionRequestService actionRequestService;

    /**
     * @param actionRequestId uuid
     * @return ResponseEntity
     */
    @GetMapping("/{actionRequestId}")
    public ResponseEntity<?> getActionRequest(@PathVariable String actionRequestId) {
        Optional<NeOneActionRequests> optional = this.actionRequestService.getWithActionRequestId(actionRequestId);
        if (!optional.isPresent()) {
            throw new ActionRequestException("Action Request not found", 404);
        }
        return ResponseEntityBuilder.ok().body(optional.get().getActionResponseBody());
    }

    /**
     *
     * @param actionRequestId uuid
     * @param status  可以传 <a href="https://onerecord.iata.org/ns/api#REQUEST_ACCEPTED"> 或者  { REQUEST_ACCEPTED }
     * @return ResponseEntity
     */
    @PutMapping("/{actionRequestId}")
    public ResponseEntity<?> updateActionRequest(@PathVariable String actionRequestId, @RequestParam("status") String status) {

        String iri = this.actionRequestService.updateActionRequest(actionRequestId, status);
        return ResponseEntityBuilder.create(204)
                .header("Type", "https://onerecord.iata.org/ns/api#ChangeRequest")
                .header(HttpHeaders.LOCATION,  iri)
                .build();
    }

    /**
     * 官方是Patch 请求
     * @param actionRequestId
     * @param status
     * @return
     */
    @PatchMapping("/{actionRequestId}")
    public ResponseEntity<?> updateActionRequestByPatchMapping(@PathVariable String actionRequestId, @RequestParam("status") String status) {

        String iri = this.actionRequestService.updateActionRequest(actionRequestId, status);
        return ResponseEntityBuilder.create(204)
                .header("Type", "https://onerecord.iata.org/ns/api#ChangeRequest")
                .header(HttpHeaders.LOCATION,  iri)
                .build();
    }

    /**
     * @param actionRequestId uuid
     * @return ResponseEntity
     */
    @DeleteMapping("/{actionRequestId}")
    public ResponseEntity<?> deleteActionRequest(@PathVariable String actionRequestId) {
        this.actionRequestService.deleteActionRequest(actionRequestId);
        return ResponseEntityBuilder.create(204).build();
    }
}
