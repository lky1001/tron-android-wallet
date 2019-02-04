package com.devband.tronlib.tronscan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Asset {

    private long netPercentage;
    private long netLimit;
    private long netRemaining;
    private long netUsed;
}
