package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 24..
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Blocks {
    @JsonProperty("total")
    private int total;

    @JsonProperty("data")
    List<Block> data;
}
