package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private String name;

    private String address;

    private long balance;

    private List<Balance> balances;

    private Bandwidth bandwidth;

    private List<Balance> tokenBalances;

    private Representative representative;

    private Frozen frozen;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Balance {
        private String name;
        private long balance;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Representative {
        private long allowance;
        private boolean enabled;
        private long lastWithDrawTime;
        private String url;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bandwidth {
        private long freeNetLimit;
        private double freeNetPercentage;
        private long freeNetRemaining;
        private long freeNetUsed;
        private long netLimit;
        private double netPercentage;
        private long netRemaining;
        private long netUsed;

        private Map<String, Bandwidth> assets;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Frozen {
        private long total;
        private List<FrozenTrx> balances;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FrozenTrx {
        private long amount;
        private long expires;
    }
}
