package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bandwidth {

    private long energyRemaining;
    private long totalEnergyLimit;
    private long totalEnergyWeight;
    private long netUsed;
    private long storageLimit;
    private long storagePercentage;
    private Map<Integer, Asset> assets;
    private long netPercentage;
    private long storageUsed;
    private long storageRemaining;
    private long freeNetLimit;
    private long energyUsed;
    private long freeNetRemaining;
    private long netLimit;
    private long netRemaining;
    private long energyLimit;
    private long freeNetUsed;
    private long totalNetWeight;
    private long freeNetPercentage;
    private long energyPercentage;
    private long totalNetLimit;
}
