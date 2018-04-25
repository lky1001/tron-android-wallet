package com.devband.tronwalletforandroid.ui.createaccount;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface CreateAccountView extends IView {

    void displayAccountInfo(String privKey, String address);

    void showProgressDialog(String title, String msg);

    void hideProgressDialog();

    void createdWallet();

    void errorCreatedWallet();
}
