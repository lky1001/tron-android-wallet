package com.devband.tronwalletforandroid.ui.accountdetail.tokenbalance;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface TokenBalanceView extends IView {

    void showLoadingDialog();
    void showServerError();
}
