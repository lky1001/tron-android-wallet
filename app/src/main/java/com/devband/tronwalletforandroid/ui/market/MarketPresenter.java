package com.devband.tronwalletforandroid.ui.market;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by user on 2018. 5. 24..
 */

public class MarketPresenter extends BasePresenter<MarketView> {

    public MarketPresenter(MarketView view) {
        super(view);
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

        TronNetwork.getInstance().getMarkets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        markets -> mView.marketDataLoadSuccess(markets),
                        t -> mView.showServerError()
                );
    }
}
