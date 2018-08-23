package com.devband.tronwalletforandroid.ui.market;

import com.devband.tronlib.TronNetwork;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class MarketActivityModule {

    @Binds
    public abstract MarketView view(MarketActivity marketActivity);

    @Provides
    static MarketPresenter provideMarketPresenter(MarketView marketView, TronNetwork tronNetwork) {
        return new MarketPresenter(marketView, tronNetwork, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
