package com.devband.tronwalletforandroid.ui.token.transfer;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface TransferView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
