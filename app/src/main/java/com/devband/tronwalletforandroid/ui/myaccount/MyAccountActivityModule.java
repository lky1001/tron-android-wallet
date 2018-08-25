package com.devband.tronwalletforandroid.ui.myaccount;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class MyAccountActivityModule {

    @Binds
    public abstract MyAccountView view(MyAccountActivity myAccountActivity);

    @Provides
    static MyAccountPresenter provideMyAccountPresenter(MyAccountView myAccountView, Tron tron,
            WalletAppManager walletAppManager) {
        return new MyAccountPresenter(myAccountView, tron, walletAppManager, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
