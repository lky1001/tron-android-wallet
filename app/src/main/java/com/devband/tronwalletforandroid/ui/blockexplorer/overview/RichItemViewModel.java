package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.dto.RichData;
import com.devband.tronlib.dto.RichInfo;
import com.devband.tronlib.dto.RichTotal;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2018. 5. 30..
 */

@Getter
public class RichItemViewModel {
    private String balanceRange;
    private float addressPercentage;
    private long trx;
    private double coinPercentage;
    private long coins;

    public RichItemViewModel(RichTotal total, RichInfo richInfo) {
        balanceRange = richInfo.getFrom() + "\n-\n" + richInfo.getTo();
        addressPercentage = richInfo.getAccounts() / (float) total.getAccounts() * 100F;
        coinPercentage = richInfo.getBalance() / total.getCoins() * 100L;
        coins = richInfo.getBalance();
    }
}
