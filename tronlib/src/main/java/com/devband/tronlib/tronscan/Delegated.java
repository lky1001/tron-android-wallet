package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Delegated {

    private List<Object> sentDelegatedBandwidth;
    private List<Object> sentDelegatedResource;
    private List<Object> receivedDelegatedResource;
    private List<Object> receivedDelegatedBandwidth;
}
