package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 24..
 */

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Market {

    @JsonProperty("rank")
    private int rank;

    @JsonProperty("name")
    private String name;

    @JsonProperty("pair")
    private String pair;

    @JsonProperty("link")
    private String link;

    @JsonProperty("volume")
    private long volume;

    @JsonProperty("volumePercentage")
    private float volumePercentage;

    @JsonProperty("volumeNative")
    private long volumeNative;

    @JsonProperty("price")
    private float price;
}
