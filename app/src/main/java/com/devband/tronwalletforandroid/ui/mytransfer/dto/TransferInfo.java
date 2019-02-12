package com.devband.tronwalletforandroid.ui.mytransfer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 17..
 */

@Getter
@Setter
public class TransferInfo {

    private String hash;
    private long block;
    private long timestamp;
    private String transferFromAddress;
    private String transferToAddress;
    private boolean isSend;
    private long amount;
    private String tokenName;
    private boolean confirmed;
    private long total;
    private int precision;
}
