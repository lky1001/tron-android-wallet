package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Representative {

    private String address;

    @JsonProperty("change_day")
    private long changeDay;

    @JsonProperty("change_hour")
    private long changeHour;

    private boolean hasPage;

    private String url;

    private Long votes;
}
