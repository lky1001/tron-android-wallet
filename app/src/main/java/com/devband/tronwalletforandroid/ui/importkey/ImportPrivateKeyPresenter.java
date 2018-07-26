package com.devband.tronwalletforandroid.ui.importkey;

import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImportPrivateKeyPresenter extends BasePresenter<ImportPrivateKeyView> {

    public ImportPrivateKeyPresenter(ImportPrivateKeyView view) {
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

    public void createWallet(String privateKey, String password) {
        Single.fromCallable(() -> {
            int result = WalletAppManager.getInstance(mContext).createWallet(password);

            if (result == WalletAppManager.SUCCESS) {
                result = Tron.getInstance(mContext).registerAccount(Constants.PREFIX_ACCOUNT_NAME, privateKey, password).blockingGet();
                if (result != Tron.SUCCESS) {
                    return result;
                }

                result = Tron.getInstance(mContext).login(password);

                if (result != Tron.SUCCESS) {
                    return result;
                }

                return Tron.SUCCESS;
            } else if (result == WalletAppManager.ERROR) {
                //mView.passwordError();
            }
            return result;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    }
}
