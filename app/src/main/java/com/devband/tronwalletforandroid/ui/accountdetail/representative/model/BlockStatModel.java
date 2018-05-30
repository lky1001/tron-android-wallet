package com.devband.tronwalletforandroid.ui.accountdetail.representative.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 29..
 */

@Getter
@Setter
public class BlockStatModel implements BaseModel {

    private String address;
    private String mediaUrl;
    private long transactionIn;
    private long transactionOut;

    public BlockStatModel(String address, String mediaUrl, long transactionIn, long transactionOut) {
        this.address = address;
        this.mediaUrl = mediaUrl;
        this.transactionIn = transactionIn;
        this.transactionOut = transactionOut;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.BLOCK_STAT;
    }
}
