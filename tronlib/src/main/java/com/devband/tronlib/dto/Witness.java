package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Witness {

    private int lastRanking;

    private int realTimeRanking;

    private String address;

    private String name;

    private String url;

    private boolean hasPage;

    private long lastCycleVotes;

    private long realTimeVotes;

    private long changeVotes;

    private double votesPercentage;

    @JsonProperty("change_cycle")
    private int changeCycle;
}
