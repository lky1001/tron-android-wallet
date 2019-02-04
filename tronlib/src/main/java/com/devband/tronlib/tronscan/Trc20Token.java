package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trc20Token {

    private String symbol;
    private double balance;
    private int decimals;
    private String name;

    @JsonProperty("contract_address")
    private String contractAddress;
}
