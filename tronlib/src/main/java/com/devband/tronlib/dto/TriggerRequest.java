package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TriggerRequest {

    @JsonProperty("contract_address")
    private String contractAddress;

    @JsonProperty("owner_address")
    private String ownerAddress;

    @JsonProperty("function_selector")
    private String functionSelector;

    @JsonProperty("fee_limit")
    private long feeLimit;

    @JsonProperty("call_value")
    private long callValue;

    private String parameter;
}
