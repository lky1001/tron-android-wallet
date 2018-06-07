package com.devband.tronwalletforandroid.ui.createwallet;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface CreateWalletView extends IView {

    void createdWallet();

    void passwordError();

    void registerWalletError();
}
