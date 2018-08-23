package com.devband.tronwalletforandroid.ui.market;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Scheduler;

/**
 * Created by user on 2018. 5. 24..
 */

public class MarketPresenter extends BasePresenter<MarketView> {

    private TronNetwork mTronNetwork;
    private Scheduler mProcessScheduler;
    private Scheduler mObserverScheduler;

    public MarketPresenter(MarketView view, TronNetwork tronNetwork,
            Scheduler processScheduler, Scheduler observerScheduler) {
        super(view);
        this.mTronNetwork = tronNetwork;
        this.mProcessScheduler = processScheduler;
        this.mObserverScheduler = observerScheduler;
    }

    @Override
    public void onCreate() {
        loadMarket();
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

    private void loadMarket() {
        mView.showLoadingDialog();

        this.mTronNetwork
        .getMarkets()
        .subscribeOn(mProcessScheduler)
        .observeOn(mObserverScheduler)
        .subscribe(
                markets -> mView.marketDataLoadSuccess(markets),
                t -> mView.showServerError()
        );
    }
}
