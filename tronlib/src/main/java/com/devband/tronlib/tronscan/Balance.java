package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Balance {

    private long balance;
    private String name;

    @JsonProperty("owner_address")
    private String ownerAddress;
}
