package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    private String hash;

    private long block;

    private boolean confirmed;

    private Map<String, Object> contractData;

    private int contractType;

    private String ownerAddress;

    private long timestamp;
}
