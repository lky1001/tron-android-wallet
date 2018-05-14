package com.devband.tronwalletforandroid.ui.createwallet;

import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.WalletAppManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class CreateWalletPresenter extends BasePresenter<CreateWalletView> {

    public CreateWalletPresenter(CreateWalletView view) {
        super(view);
    }

    @Override
    public void onCreate() {
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

    public void createWallet(String password) {
        int result = WalletAppManager.getInstance(mContext).createWallet(password);

        if (result == WalletAppManager.SUCCESS) {
            result = Tron.getInstance(mContext).registerAccount(Constants.PREFIX_ACCOUNT_NAME, password);
            if (result != Tron.SUCCESS) {
                mView.registerWalletError();
                return;
            }

            result = Tron.getInstance(mContext).login(password);
            if (result != Tron.SUCCESS) {
                mView.passwordError();
                return;
            }

            mView.createdWallet();
        } else if (result == WalletAppManager.ERROR) {
            mView.passwordError();
        }
    }
}
