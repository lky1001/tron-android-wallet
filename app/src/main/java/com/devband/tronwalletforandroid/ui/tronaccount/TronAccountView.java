package com.devband.tronwalletforandroid.ui.tronaccount;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface TronAccountView extends IView {

    void showLoadingDialog();

    void displayTronAccountInfo(int tronAccountCount, long highestTrx);

    void showServerError();
}
