package com.devband.tronwalletforandroid.ui.representative;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface RepresentativeView extends IView {

    void showLoadingDialog();

    void displayRepresentativeInfo(int witnessCount, long highestVotes);

    void showServerError();
}
