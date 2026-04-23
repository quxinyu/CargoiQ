package com.efreight.base.module.one.record.neone.model.entity;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.efreight.base.module.one.record.neone.handler.FastjsonListTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@TableName("ne_one_shipment_data")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeOneShipmentData implements Serializable {

    @TableId
    private Long id;

    @JsonProperty("MawbCode")
    private String mawbCode;

    @JsonProperty("TotalPieceQuantity")
    private String totalPieceQuantity;

    @JsonProperty("TotalGrossWeight")
    private String totalGrossWeight;

    @JsonProperty("VolumnAmount")
    private String volumnAmount;

    @JsonProperty("TotalChargeableWeight")
    private String totalChargeableWeight;

    @JsonProperty("WeightTotalChargeAmount")
    private String weightTotalChargeAmount;

    @JsonProperty("TotalPrepaidChargeAmount")
    private String totalPrepaidChargeAmount;

    @JsonProperty("PrepaidIndicator")
    private String prepaidIndicator;

    @JsonProperty("DeclaredValueForCarriageAmount")
    private String declaredValueForCarriageAmount;

    @JsonProperty("DeclaredValueForCustomsAmount")
    private String declaredValueForCustomsAmount;

    @JsonProperty("InsuranceValueAmount")
    private String insuranceValueAmount;

    @JsonProperty("SourceCurrencyCode")
    private String sourceCurrencyCode;

    @JsonProperty("TransportPaymentMethodCode")
    private String transportPaymentMethodCode;

    @JsonProperty("ConsignorPartyAccountNo")
    private String consignorPartyAccountNo;

    @JsonProperty("ConsignorPartyName")
    private String consignorPartyName;

    @JsonProperty("ConsignorPartyTelephone")
    private String consignorPartyTelephone;

    @JsonProperty("ConsignorPartyFax")
    private String consignorPartyFax;

    @JsonProperty("ConsignorPartyPostcodeCode")
    private String consignorPartyPostcodeCode;

    @JsonProperty("ConsignorPartyStreetName")
    private String consignorPartyStreetName;

    @JsonProperty("ConsignorPartyCityName")
    private String consignorPartyCityName;

    @JsonProperty("ConsignorPartyCountryID")
    private String consignorPartyCountryId;

    @JsonProperty("ConsignorPartyCountrySubDivisionID")
    private String consignorPartyCountrySubDivisionId;

    @JsonProperty("ConsigneePartyAccountNo")
    private String consigneePartyAccountNo;

    @JsonProperty("ConsigneePartyName")
    private String consigneePartyName;

    @JsonProperty("ConsigneePartyTelephone")
    private String consigneePartyTelephone;

    @JsonProperty("ConsigneePartyFax")
    private String consigneePartyFax;

    @JsonProperty("ConsigneePartyPostcodeCode")
    private String consigneePartyPostcodeCode;

    @JsonProperty("ConsigneePartyStreetName")
    private String consigneePartyStreetName;

    @JsonProperty("ConsigneePartyCityName")
    private String consigneePartyCityName;

    @JsonProperty("ConsigneePartyCountryID")
    private String consigneePartyCountryId;

    @JsonProperty("ConsigneePartyCountrySubDivisionID")
    private String consigneePartyCountrySubDivisionId;

    @JsonProperty("FreightForwarderPartyName")
    private String freightForwarderPartyName;

    @JsonProperty("FreightForwarderPartyAddress")
    private String freightForwarderPartyAddress;

    @JsonProperty("FreightForwarderPartyIATACode")
    private String freightForwarderPartyIataCode;

    @JsonProperty("FreightForwarderPartyAccountNo")
    private String freightForwarderPartyAccountNo;

    @JsonProperty("DepPort")
    private String depPort;

    @JsonProperty("ArrPort")
    private String arrPort;

    @JsonProperty("FlightNo")
    private String flightNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("FlightDate")
    private LocalDate flightDate;

    @JsonProperty("FlightNo2")
    private String flightNo2;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("FlightDate2")
    private LocalDate flightDate2;

    @JsonProperty("ArrivalLocationID1")
    private String arrivalLocationId1;

    @JsonProperty("CarrierID1")
    private String carrierId1;

    @JsonProperty("ArrivalLocationID2")
    private String arrivalLocationId2;

    @JsonProperty("CarrierID2")
    private String carrierId2;

    @JsonProperty("ArrivalLocationID3")
    private String arrivalLocationId3;

    @JsonProperty("CarrierID3")
    private String carrierId3;

    @JsonProperty("SpecialServiceRequest")
    private String specialServiceRequest;

    @JsonProperty("OtherServiceInformation")
    private String otherServiceInformation;

    @JsonProperty("SPHCode")
    private String sphCode;

    @JsonProperty("PieceQuantity")
    private String pieceQuantity;

    @JsonProperty("PackageQuantity")
    private String packageQuantity;

    @JsonProperty("GrossWeightUnitCode")
    private String grossWeightUnitCode;

    @JsonProperty("GrossWeight")
    private String grossWeight;

    @JsonProperty("RateClass")
    private String rateClass;

    @JsonProperty("CommodityItemCode")
    private String commodityItemCode;

    @JsonProperty("ChargeableWeight")
    private String chargeableWeight;

    @JsonProperty("AppliedRate")
    private String appliedRate;

    @JsonProperty("AppliedAmount")
    private String appliedAmount;

    @JsonProperty("NatureIdentificationTransportCargo")
    private String natureIdentificationTransportCargo;

    @JsonProperty("SignatoryConsignorAuthentication")
    private String signatoryConsignorAuthentication;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("ActualDateTime")
    private LocalDate actualDateTime;

    @JsonProperty("IssueAuthenticationLocation")
    private String issueAuthenticationLocation;

    @JsonProperty("Signatory")
    private String signatory;

    @JsonProperty("HSCode")
    private String hsCode;

    @JsonProperty("VolumeCode")
    private String volumeCode;

    @JsonProperty("Recipient")
    private String recipient;

    @JsonProperty("Sender")
    private String sender;

    @JsonProperty("LoId")
    private String loId;

    @JsonProperty("Dimensions")
    @TableField(typeHandler = FastjsonListTypeHandler.class)
    private List<Map<String, Object>> dimensions;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String checkResult;

    private String checkStatus;

    private String aiCheckStatus;

}
