package com.efreight.base.module.one.record.neone.model.onerecord;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * IATA ONE Record LogisticsEvent 实体类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 序列化时忽略 null 字段
public class LogisticsEventFSU {

    @JsonProperty("@type")
    private String type = "cargo:LogisticsEvent";

    @JsonProperty("@id")
    private String id;

    @JsonProperty("@context")
    private Map<String, String> context;

    @JsonProperty("cargo:creationDate")
    private String creationDate;

    @JsonProperty("cargo:eventCode")
    private CodeListElement eventCode;

    @JsonProperty("cargo:exceptionHandlingCodes")
    private List<ExceptionHandlingCode> exceptionHandlingCodes;

    @JsonProperty("cargo:eventFor")
    private LogisticsObject eventFor;

    @JsonProperty("cargo:eventLocation")
    private Location eventLocation;

    @JsonProperty("cargo:eventName")
    private String eventName;

    @JsonProperty("cargo:eventTimeType")
    private String eventTimeType;

    @JsonProperty("cargo:externalReferences")
    private ExternalReference externalReferences;

    @JsonProperty("cargo:recordingActor")
    private Actor recordingActor;

    @JsonProperty("cargo:recordingOrganization")
    private Organization recordingOrganization;


    // ================= 嵌套的内部类定义 =================

    @Data
    public static class CodeListElement {
        @JsonProperty("@type")
        private String type = "cargo:CodeListElement";

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonProperty("cargo:code")
        private String code;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonProperty("cargo:codeDescription")
        private String codeDescription;
    }

    @Data
    public static class ExceptionHandlingCode{
        @JsonProperty("@type")
        private String type = "cargo:EHC";

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonProperty("cargo:Code")
        private String code;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonProperty("cargo:codeDescription")
        private String codeDescription;
    }

    @Data
    public static class LogisticsObject {
        @JsonProperty("@id")
        private String id;

        @JsonProperty("@type")
        private String type = "cargo:LogisticsObject";

        @JsonProperty("cargo:waybill")
        private Waybill waybill;
    }

    @Data
    public static class Waybill {
        @JsonProperty("@type")
        private String type = "cargo:Waybill";

        @JsonProperty("cargo:arrivalLocation")
        private Location arrivalLocation;

        @JsonProperty("cargo:departureLocation")
        private Location departureLocation;

        @JsonProperty("cargo:waybillNumber")
        private String waybillNumber;

        @JsonProperty("cargo:waybillPrefix")
        private String waybillPrefix;
    }

    @Data
    public static class Location {
        @JsonProperty("@type")
        private String type = "cargo:Location";

        @JsonProperty("cargo:locationCodes")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private CodeListElement locationCodes;
    }

    @Data
    public static class ExternalReference {
        @JsonProperty("@type")
        private String type = "cargo:ExternalReference";
    }

    @Data
    public static class Actor {
        @JsonProperty("@type")
        private String type = "cargo:Actor";
    }

    @Data
    public static class Organization {
        @JsonProperty("@type")
        private String type = "cargo:Organization";

        @JsonProperty("cargo:shortName")
        private String shortName;
    }
}