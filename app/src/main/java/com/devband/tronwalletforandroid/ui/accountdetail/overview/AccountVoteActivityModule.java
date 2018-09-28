package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AccountVoteActivityModule {

    @Binds
    public abstract AccountVoteView view(AccountVoteActivity accountVoteActivity);

    @Provides
    static AccountVotePresenter provideAccountVotePresenter(AccountVoteView view, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        return new AccountVotePresenter(view, tronNetwork, rxJavaSchedulers);
    }
}
