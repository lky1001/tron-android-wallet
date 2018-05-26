package com.devband.tronwalletforandroid.ui.token.transaction;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface TransactionView extends IView {

    void showLoadingDialog();
    void showServerError();
}
