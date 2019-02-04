package com.devband.tronlib.tronscan;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Representative {

    private long lastWithDrawTime;
    private long allowance;
    private boolean enabled;
    private String url;
}