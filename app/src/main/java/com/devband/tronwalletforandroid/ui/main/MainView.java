package com.devband.tronwalletforandroid.ui.main;

import com.devband.tronwalletforandroid.ui.mvp.IView;

import org.tron.protos.Protocol;

public interface MainView extends IView {

    void displayAccountInfo(Protocol.Account account);
}
