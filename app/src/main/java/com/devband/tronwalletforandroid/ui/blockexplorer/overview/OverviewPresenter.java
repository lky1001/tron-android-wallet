package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by user on 2018. 5. 28..
 */

public class OverviewPresenter extends BasePresenter<OverviewView> {

    public OverviewPresenter(OverviewView view) {
        super(view);
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

    void dataLoad() {
        mView.showLoadingDialog();

        TronNetwork.getInstance().getTopAddressAccounts(13)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mView::overviewDataLoadSuccess,
                        t -> mView.showServerError()
                );

        TronNetwork.getInstance().getAvgBlockSize()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mView::overviewAvgBlockSize,
                        t -> mView.showServerError()
                );

        TronNetwork.getInstance().getSystemStatus()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mView::getBlockStatus,
                        t -> mView.showServerError()
                );
    }
}
