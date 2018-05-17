package com.devband.tronwalletforandroid.ui.myaccount;

import com.devband.tronwalletforandroid.ui.mvp.IView;

import org.tron.protos.Protocol;

public interface MyAccountView extends IView {

    void displayAccountInfo(Protocol.Account account);
}
