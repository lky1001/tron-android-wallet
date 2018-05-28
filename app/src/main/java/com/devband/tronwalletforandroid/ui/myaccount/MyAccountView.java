package com.devband.tronwalletforandroid.ui.myaccount;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface MyAccountView extends IView {

    void displayAccountInfo(@NonNull String address, @NonNull TronAccount account);

    void showLoadingDialog();

    void hideDialog();

    void showServerError();

    void successFreezeBalance();

    void unableToUnfreeze();
}
