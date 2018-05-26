package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenHolder {

    private String address;

    private String name;

    private long totalSupply;

    private long balance;

    private double balancePercent;
}
