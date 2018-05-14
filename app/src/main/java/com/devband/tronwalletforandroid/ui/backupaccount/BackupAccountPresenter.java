package com.devband.tronwalletforandroid.ui.backupaccount;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class BackupAccountPresenter extends BasePresenter<BackupAccountView> {

    public BackupAccountPresenter(BackupAccountView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        String address = Tron.getInstance(mContext).getLoginAddress();
        String privateKey = Tron.getInstance(mContext).getLoginPrivateKey();

        mView.displayAccountInfo(address, privateKey);
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
