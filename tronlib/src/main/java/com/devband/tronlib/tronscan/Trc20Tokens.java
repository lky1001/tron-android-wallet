package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trc20Tokens {

    private int total;

    @JsonProperty("trc20_tokens")
    private List<Trc20Token> trc20TokenList;
}
