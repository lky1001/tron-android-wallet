package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Witnesses {

    private int total;

    private long totalVotes;

    private List<Witness> data;

    private Witness fastestRise;
}
