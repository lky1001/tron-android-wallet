package com.devband.tronwalletforandroid.ui.login;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class LoginActivityModule {

    @Binds
    public abstract LoginView view(LoginActivity loginActivity);

    @Provides
    static LoginPresenter provideLoginPresenter(LoginView loginView, Tron tron,
            WalletAppManager walletAppManager) {
        return new LoginPresenter(loginView, tron, walletAppManager, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
