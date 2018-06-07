package com.devband.tronwalletforandroid.ui.backupaccount;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.ui.mvp.IView;

public interface BackupAccountView extends IView {

    void displayAccountInfo(@NonNull String address, @NonNull String privateKey);

    void startMainActivity();
}
