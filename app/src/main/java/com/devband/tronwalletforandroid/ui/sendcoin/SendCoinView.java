package com.devband.tronwalletforandroid.ui.sendcoin;

import com.devband.tronwalletforandroid.ui.mvp.IView;

import org.tron.protos.Protocol;

public interface SendCoinView extends IView {

    void sendCoinResult(boolean result);

    void invalidPassword();

    void displayAccountInfo(Protocol.Account account);

    void invalidAddress();
}
