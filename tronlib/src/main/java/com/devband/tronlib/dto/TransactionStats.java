package com.devband.tronlib.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 28..
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionStats {

    @JsonProperty("transactions")
    private long transactions;

    @JsonProperty("transactions_out")
    private long transactionsOut;

    @JsonProperty("transactions_in")
    private long transactionsIn;
}
