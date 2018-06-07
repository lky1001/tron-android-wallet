package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 17..
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transfers {
    @JsonProperty("total")
    private long total;

    @JsonProperty("data")
    private List<Transfer> data;
}
