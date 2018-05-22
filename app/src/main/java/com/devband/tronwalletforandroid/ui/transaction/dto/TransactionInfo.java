package com.devband.tronwalletforandroid.ui.transaction.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 17..
 */

@Getter
@Setter
public class TransactionInfo {

    private String hash;
    private long block;
    private long timestamp;
    private String transferFromAddress;
    private String transferToAddress;
    private boolean isSend;
    private long amount;
    private String tokenName;
    private boolean confirmed;
}
