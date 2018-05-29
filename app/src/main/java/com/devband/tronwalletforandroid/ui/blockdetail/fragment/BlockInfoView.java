package com.devband.tronwalletforandroid.ui.blockdetail.fragment;

import android.support.annotation.NonNull;

import com.devband.tronlib.dto.Block;
import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface BlockInfoView extends IView {
    void showLoadingDialog();
    void showServerError();
    void finishLoading(@NonNull Block block);
}
