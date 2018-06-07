package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 24..
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {

    @JsonProperty("number")
    private long number;

    @JsonProperty("hash")
    private String hash;

    @JsonProperty("size")
    private int size;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("txTrieRoot")
    private String txTrieRoot;

    @JsonProperty("parentHash")
    private String parentHash;

    @JsonProperty("witnessId")
    private int witnessId;

    @JsonProperty("witnessAddress")
    private String witnessAddress;

    @JsonProperty("nrOfTrx")
    private int nrOfTrx;

    @JsonProperty("confirmed")
    private boolean confirmed;
}
