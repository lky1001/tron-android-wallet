package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface OverviewView extends IView {

    void showLoadingDialog();
    void showServerError();
    void finishLoading(@NonNull TronAccount account);
}
