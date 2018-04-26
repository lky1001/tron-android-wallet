package com.devband.tronwalletforandroid.ui.backupwallet;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class BackupWalletPresenter extends BasePresenter<BackupWalletView> {

    public BackupWalletPresenter(BackupWalletView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        String address = Tron.getInstance(mContext).getAddress();
        String privateKey = Tron.getInstance(mContext).getPrivateKey();

        mView.displayWalletInfo(address, privateKey);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
