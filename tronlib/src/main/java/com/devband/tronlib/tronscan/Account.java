package com.devband.tronlib.tronscan;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

    @JsonProperty("trc20token_balances")
    private List<Trc20Token> trc20TokenBalances;

    private List<Object> allowExchange;
    private String address;
    @JsonProperty("frozen_supply")
    private List<Object> frozenSupply;
    private Bandwidth bandwidth;
    private int accountType;
    private List<Object> exchanges;
    private Frozen frozen;
    private AccountResource accountResource;
    private List<Balance> tokenBalances;
    private List<Balance> balances;
    private long balance;
    private String name;
    private Delegated delegated;
    private Representative representative;

}