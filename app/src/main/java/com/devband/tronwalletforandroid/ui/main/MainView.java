package com.devband.tronwalletforandroid.ui.main;

import android.support.annotation.NonNull;

import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronwalletforandroid.ui.mvp.IView;

import org.tron.protos.Protocol;

public interface MainView extends IView {

    void displayAccountInfo(@NonNull Protocol.Account account);

    void setTronMarketInfo(CoinMarketCap coinMarketCap);

    void showInvalidPasswordMsg();

    void successCreateWallet();

    void successImportWallet();

    void failCreateWallet();

    void duplicatedWallet();
}
