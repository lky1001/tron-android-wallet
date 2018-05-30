package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 17..
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfer {

    private String id;

    @JsonProperty("transactionHash")
    private String hash;

    @JsonProperty("block")
    private long block;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("transferFromAddress")
    private String transferFromAddress;

    @JsonProperty("transferToAddress")
    private String transferToAddress;

    @JsonProperty("amount")
    private long amount;

    @JsonProperty("tokenName")
    private String tokenName;

    @JsonProperty("confirmed")
    private boolean confirmed;
}
