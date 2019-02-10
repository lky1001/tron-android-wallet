package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trc20Token {

    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("issue_ts")
    private long issueTs;

    private String symbol;

    @JsonProperty("total_supply")
    private long totalSupply;

    @JsonProperty("total_supply_str")
    private String totalSupplyStr;

    @JsonProperty("home_page")
    private String homepage;

    @JsonProperty("token_desc")
    private String tokenDesc;

    @JsonProperty("git_hub")
    private String github;

    @JsonProperty("total_supply_with_decimals")
    private String totalSupplyWithDecimals;

    @JsonProperty("decimals")
    private int precision;

    private String name;

    @JsonProperty("white_paper")
    private String whitePaper;

    @JsonProperty("contract_address")
    private String contractAddress;
}
