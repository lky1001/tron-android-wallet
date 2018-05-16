package com.devband.tronwalletforandroid.network.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class TransactionInfo {
    private String hash;
    private int block;
    private long timestamp;
    private String transferFromAddress;
    private String transferToAddress;
    private long amount;
    private String tokenName;


}
