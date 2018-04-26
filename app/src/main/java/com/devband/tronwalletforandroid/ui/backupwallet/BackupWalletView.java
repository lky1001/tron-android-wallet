package com.devband.tronwalletforandroid.ui.backupwallet;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface BackupWalletView extends IView {

    void displayWalletInfo(@NonNull String address, @NonNull String privateKey);
}
