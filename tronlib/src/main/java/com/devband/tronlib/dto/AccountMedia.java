package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 29..
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountMedia {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("image")
    private String imageUrl;

}
