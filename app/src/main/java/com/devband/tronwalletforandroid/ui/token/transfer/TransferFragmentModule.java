package com.devband.tronwalletforandroid.ui.token.transfer;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class TransferFragmentModule {

    @Binds
    public abstract TransferView view(TransferFragment fragment);

    @Provides
    static TransferPresenter provideHolderPresenter(TransferView view, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new TransferPresenter(view, tronNetwork, rxJavaSchedulers);
    }
}