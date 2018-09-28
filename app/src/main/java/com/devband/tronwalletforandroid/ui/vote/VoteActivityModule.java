package com.devband.tronwalletforandroid.ui.vote;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class VoteActivityModule {

    @Binds
    public abstract VoteView view(VoteActivity voteActivity);

    @Provides
    static VotePresenter provideVotePresenter(VoteView view, Tron tron, TronNetwork tronNetwork,
            WalletAppManager walletAppManager, RxJavaSchedulers rxJavaSchedulers) {
        return new VotePresenter(view, tron, tronNetwork, walletAppManager, rxJavaSchedulers);
    }
}
