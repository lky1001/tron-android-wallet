package com.devband.tronwalletforandroid.ui.blockexplorer.account;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface AccountView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
