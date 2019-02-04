package com.devband.tronwalletforandroid.ui.mytransfer;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.TokenManager;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class MyTransferActivityModule {

    @Binds
    public abstract TransferView view(TransferActivity transferActivity);

    @Provides
    static TransferPresenter provideTransferPresenter(TransferView transferView, Tron tron,
            TronNetwork tronNetwork, TokenManager tokenManager, RxJavaSchedulers rxJavaSchedulers) {
        return new TransferPresenter(transferView, tron, tronNetwork, tokenManager, rxJavaSchedulers);
    }
}
