package com.devband.tronwalletforandroid.ui.accountdetail.representative;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface RepresentativeView extends IView {

    void showLoadingDialog();
    void showServerError();
}
