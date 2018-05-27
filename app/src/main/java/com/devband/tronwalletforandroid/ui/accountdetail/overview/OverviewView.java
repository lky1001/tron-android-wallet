package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface OverviewView extends IView {

    void showLoadingDialog();
    void showServerError();
}
