package com.devband.tronwalletforandroid.ui.witness;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface WitnessView extends IView {

    void showLoadingDialog();

    void displayWitnessInfo(int witnessCount, long highestVotes);

    void showServerError();
}
