package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {

    private String id;

    private long block;

    private int num;

    private String name;

    private String abbr;

    private String description;

    private long startTime;

    private long endTime;

    private int price;

    private long totalSupply;

    private long issued;

    private double issuedPercentage;

    private double percentage;

    private String ownerAddress;

    private String transaction;

    private long trxNum;

    private String url;

    private int voteScore;

    private long created;

    private long dateCreated;

    private long nrOfTokenHolders;

    private long totalTransactions;

}