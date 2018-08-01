package com.devband.tronwalletforandroid.ui.importkey;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface ImportPrivateKeyView extends IView {

    void createdWallet();

    void passwordError();

    void registerWalletError();
}
