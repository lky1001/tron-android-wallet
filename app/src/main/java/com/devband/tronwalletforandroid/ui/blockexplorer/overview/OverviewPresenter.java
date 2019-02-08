package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

/**
 * Created by user on 2018. 5. 28..
 */

public class OverviewPresenter extends BasePresenter<OverviewView> {

    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public OverviewPresenter(OverviewView view, TronNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @Override
    public void onCreate() {

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

    void chartDataLoad() {
        mView.showLoadingDialog();

        mTronNetwork.getAccounts(0, 10, "-balance")
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(
                        mView::overviewDataLoadSuccess,
                        t -> mView.showServerError()
                );
    }
}
