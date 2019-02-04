package com.devband.tronlib.tronscan;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Delegated {

    private List<Object> sentDelegatedBandwidth;
    private List<Object> sentDelegatedResource;
    private List<Object> receivedDelegatedResource;
    private List<Object> receivedDelegatedBandwidth;
}
