package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoinMarketCap {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("price_usd")
    private String priceUsd;

    @JsonProperty("price_btc")
    private String priceBtc;

    @JsonProperty("24h_volume_usd")
    private String _24hVolumeUsd;

    @JsonProperty("market_cap_usd")
    private String marketCapUsd;

    @JsonProperty("available_supply")
    private String availableSupply;

    @JsonProperty("total_supply")
    private String totalSupply;

    @JsonProperty("max_supply")
    private Object maxSupply;

    @JsonProperty("percent_change_1h")
    private String percentChange1h;

    @JsonProperty("percent_change_24h")
    private String percentChange24h;

    @JsonProperty("percent_change_7d")
    private String percentChange7d;

    @JsonProperty("last_updated")
    private String lastUpdated;
}
