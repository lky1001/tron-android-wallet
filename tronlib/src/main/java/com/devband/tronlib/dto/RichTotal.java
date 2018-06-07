package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 30..
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RichTotal {

    @JsonProperty("accounts")
    private int accounts;

    @JsonProperty("coins")
    private double coins;
}
