package com.devband.tronwalletforandroid.ui.createaccount;

import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class CreateAccountPresenter extends BasePresenter<CreateAccountView> {

    public CreateAccountPresenter(CreateAccountView view) {
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

    public void createAccount(String password) {
        int result = AccountManager.getInstance(mContext).createAccount(password);

        if (result == AccountManager.SUCCESS) {
            result = Tron.getInstance(mContext).registerWallet("Wallet1", password);
            if (result != Tron.SUCCESS) {
                mView.registerWalletError();
                return;
            }

            result = Tron.getInstance(mContext).login(password);
            if (result != Tron.SUCCESS) {
                mView.passwordError();
                return;
            }

            mView.createdAccount();
        } else if (result == AccountManager.ERROR) {
            mView.passwordError();
        }
    }
}
