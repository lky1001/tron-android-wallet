package com.devband.tronwalletforandroid.ui.token;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface TokenView extends IView {

    void showLoadingDialog();

    void showServerError();

    void finishLoading(int total);
}
