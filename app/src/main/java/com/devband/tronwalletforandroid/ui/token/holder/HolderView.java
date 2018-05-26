package com.devband.tronwalletforandroid.ui.token.holder;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface HolderView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
