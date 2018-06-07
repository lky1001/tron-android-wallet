package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountVote {

    private String id;

    private long block;

    private String candidateAddress;

    private String candidateName;

    private String candidateUrl;

    private String timestamp;

    private String transaction;

    private String voterAddress;

    private long voterAvailableVotes;

    private long votes;

    private long totalVotes;
}
