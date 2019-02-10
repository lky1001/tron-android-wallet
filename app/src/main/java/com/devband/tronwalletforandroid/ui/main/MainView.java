package com.devband.tronwalletforandroid.ui.main;

import android.support.annotation.NonNull;

import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface MainView extends IView {

    void showLoadingDialog();

    void displayAccountInfo(@NonNull TronAccount account);

    void setTronMarketInfo(CoinMarketCap coinMarketCap);

    void showInvalidPasswordMsg();

    void successCreateAccount();

    void successImportAccount();

    void failCreateAccount();

    void duplicatedAccount();

    void connectionError();

    void changePasswordResult(boolean result);

    void showChangePasswordDialog();

    void goToIntroActivity();

    void resultAddTrc20(int result);

    void finishSyncTrc20();

    void showSyncTrc20Loading();
}
