package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface VoteView extends IView {

    void finishLoading(long total);
    void showLoadingDialog();
    void showServerError();
}
