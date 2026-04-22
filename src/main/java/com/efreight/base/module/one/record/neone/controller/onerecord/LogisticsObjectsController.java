package com.efreight.base.module.one.record.neone.controller.onerecord;

import com.efreight.base.module.one.record.neone.annotations.Authenticated;
import com.efreight.base.module.one.record.neone.constants.BaseConstants;
import com.efreight.base.module.one.record.neone.exception.MissingMetadataException;
import com.efreight.base.module.one.record.neone.exception.SubjectNotFoundException;
import com.efreight.base.module.one.record.neone.model.onerecord.IdProvider;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObject;
import com.efreight.base.module.one.record.neone.model.onerecord.LogisticsObjectMetadata;
import com.efreight.base.module.one.record.neone.model.onerecord.Snapshot;
import com.efreight.base.module.one.record.neone.security.AccessMode;
import com.efreight.base.module.one.record.neone.security.AccessObject;
import com.efreight.base.module.one.record.neone.security.AclSecured;
import com.efreight.base.module.one.record.neone.service.onerecord.LogisticsObjectMetadataService;
import com.efreight.base.module.one.record.neone.service.onerecord.LogisticsObjectService;
import com.efreight.base.module.one.record.neone.service.onerecord.NeoneId;
import com.efreight.base.module.one.record.neone.service.onerecord.SnapshotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

/**
 * 国际航协ONERECORD逻辑
 *
 * @author quxinyu
 * @since 2026-01-09
 */
@Slf4j
@Authenticated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/" + LogisticsObjectsController.RESOURCE_NAME,
        consumes = {"application/ld+json", "application/x-turtle", "text/turtle"},
        produces = {"application/ld+json", "application/x-turtle", "text/turtle"})
public class LogisticsObjectsController {

    public static final String RESOURCE_NAME = "v2/logistics-objects";
    public static final String LATEST_REVISION_HEADER = "Latest-Revision";
    public static final String REVISION_HEADER = "Revision";
    public static final String LAST_MODIFIED_HEADER = "Last-Modified";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String LOCATION_HEADER = "Location";
    public static final String TYPE_HEADER = "Type";

    @Value("${default-language:en-US}")
    String defaultLanguage;
    private final LogisticsObjectMetadataService metadataService;

    private final LogisticsObjectService logisticsObjectService;

    private final SnapshotService snapshotService;

    private final IdProvider idProvider;

    @PostMapping()
    public ResponseEntity<?> createLogisticsObject(@RequestBody LogisticsObjectList logisticsObjects,
                                                   @RequestParam(name = "public", required = false) boolean publicAccess) {
        logisticsObjectService.createLogisticsObjects(logisticsObjects, publicAccess);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, logisticsObjects.getRootIRI().stringValue());
        return ResponseEntity
                .status(201)
                .headers(headers)
                .build();
    }

    @AclSecured(@AccessMode(AccessMode.READ))
    @GetMapping(value = "{id}", produces = BaseConstants.CONTENT_TYPE_LD_JSON)
    public GetLogisticsObjectResponse getLogisticsObject(@AccessObject(AccessObject.Type.LOGISTICS_OBJECT_ID) @PathVariable("id") String id,
                                                         @RequestParam(name = "at", required = false) String at,
                                                         @RequestParam(name = "embedded", required = false) boolean embedded) {
        log.debug("Handling GET request with id [{}] at timestamp [{}]", id, at);
        NeoneId loId = idProvider.getUriForLoId(id);
        Instant ts = at != null ? DateTimeConverter.convert(at) : null;
        LogisticsObject logisticsObject;
        Optional<Snapshot> snapshot;

        if (ts == null) {
            // 查询LO最新版本.
            logisticsObject = logisticsObjectService.getLogisticsObject(loId.getUri(), embedded);
            snapshot = Optional.empty();
        } else {
            snapshot = snapshotService.getSnapshot(loId.getIri(), ts);
            logisticsObject = snapshot.map(LogisticsObjectService::toLogisticsObject)
                    .orElseThrow(() -> new SubjectNotFoundException("Subject Not Found  " +loId.getIri().stringValue()));
        }
        Optional<Snapshot> latestSnapshot = snapshotService.getLatestSnapshot(loId.getIri());
        LogisticsObjectMetadata meta = metadataService.getMetadataOf(logisticsObject.iri())
                .orElseThrow(() ->
                        new MissingMetadataException("Missing metadata for [" + logisticsObject.iri().stringValue() + "]"));
        Integer latestRevision = meta.getRevision();
        Integer revision = snapshot.map(Snapshot::revision).orElse(latestRevision);
        Instant lastModified = latestSnapshot.map(Snapshot::at).orElse(meta.getCreatedAt());

        return new GetLogisticsObjectResponse()
                .withLogisticsObject(logisticsObject)
                .withHeader(LOCATION_HEADER, logisticsObject.iri())
                .withHeader(REVISION_HEADER, revision)
                .withHeader(LATEST_REVISION_HEADER, latestRevision)
                .withHeader(LAST_MODIFIED_HEADER, lastModified)
                // For the time being, default language used:
                .withHeader(CONTENT_LANGUAGE, defaultLanguage);
    }

    public static class GetLogisticsObjectResponse {
        private LogisticsObject logisticsObject;
        private final MultiValueMap<String, Object> headers = new LinkedMultiValueMap<>();

        public GetLogisticsObjectResponse withLogisticsObject(LogisticsObject lo) {
            this.logisticsObject = lo;
            return this;
        }

        public GetLogisticsObjectResponse withHeader(String key, Object value) {
            this.headers.add(key, value);
            return this;
        }

        public LogisticsObject getLogisticsObject() {
            return logisticsObject;
        }

        public MultiValueMap<String, Object> getHeaders() {
            return headers;
        }
    }

}
