package com.devband.tronwalletforandroid.ui.createwallet;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface CreateWalletView extends IView {

    void displayAccountInfo(String privKey, String address);

    void showProgressDialog(String title, String msg);

    void hideProgressDialog();

    void createdWallet();

    void errorCreatedWallet();
}
