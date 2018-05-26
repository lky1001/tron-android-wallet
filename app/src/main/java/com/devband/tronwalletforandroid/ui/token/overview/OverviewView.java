package com.devband.tronwalletforandroid.ui.token.overview;

import android.support.annotation.NonNull;

import com.devband.tronlib.dto.Token;
import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface OverviewView extends IView {

    void tokenInfoLoadSuccess(@NonNull Token token);
    void showLoadingDialog();
    void showServerError();
}
