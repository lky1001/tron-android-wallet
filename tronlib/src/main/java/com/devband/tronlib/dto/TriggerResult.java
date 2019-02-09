package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TriggerResult {

    @JsonProperty("constant_result")
    private List<String> constantResult;

    private Map<String, Object> result;

    private Map<String, Object> transaction;

    @JsonProperty("txid")
    private String txId;
}
