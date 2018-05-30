package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferStats {

    // transactions
    private List<Stat> total;

    // transfers
    private List<Stat> value;
}
