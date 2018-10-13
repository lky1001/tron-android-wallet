package com.devband.tronwalletforandroid.ui.sendtoken;

import com.devband.tronwalletforandroid.ui.mvp.IView;

import org.tron.protos.Protocol;

public interface SendTokenView extends IView {

    void sendTokenResult(boolean result);

    void invalidPassword();

    void displayAccountInfo(Protocol.Account account);

    void invalidAddress();

    void connectionError();
}
