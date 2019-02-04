package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenInfo {

    private String name;
    private String ownerAddress;

    private long totalTransactions;
    private String country;
    private long tokenID;
    private long participated;
    private long precision;
    private long num;
    private long available;
    private String reputation;
    private String description;
    private double issuedPercentage;
    private long nrOfTokenHolders;
    private long voteScore;
    private long dateCreated;
    private long price;
    private double percentage;
    private long startTime;
    private long id;
    private double issued;
    private long trxNum;
    private String abbr;
    private String website;
    private String github;
    private long availableSupply;
    private long totalSupply;
    private long index;
    private long frozenTotal;
    private List<Object> frozen;
    private long canShow;
    private long remaining;
    private String url;
    private double frozenPercentage;
    private String imgUrl;
    private boolean isBlack;
    private float remainingPercentage;
    private long endTime;
    private String white_paper;

    @JsonProperty("social_media")
    private List<SocialMedia> socialMedia;
}
