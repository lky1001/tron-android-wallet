package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TronAccount {

    private String name;

    private String address;

    private long totalSupply;

    private long availableSypply;

    private long balance;

    private double balancePercent;

    private long dateCreated;

    private long dateUpdated;

    private long power;

    private Map<String, Long> tokenBalances;
}
