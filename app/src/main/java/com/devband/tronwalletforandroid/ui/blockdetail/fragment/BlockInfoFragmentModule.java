package com.devband.tronwalletforandroid.ui.blockdetail.fragment;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class BlockInfoFragmentModule {

    @Binds
    public abstract BlockInfoView view(BlockInfoFragment fragment);

    @Provides
    static BlockInfoPresenter provideBlockInfoPresenter(BlockInfoView view, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new BlockInfoPresenter(view, tronNetwork, rxJavaSchedulers);
    }
}