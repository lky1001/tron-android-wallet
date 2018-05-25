package com.devband.tronwalletforandroid.ui.token.overview;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface OverviewView extends IView {

    void tokenInfoLoadSuccess();
    void showLoadingDialog();
    void showServerError();
}
