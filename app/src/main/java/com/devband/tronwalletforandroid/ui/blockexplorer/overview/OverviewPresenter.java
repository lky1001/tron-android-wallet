package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Stat;
import com.devband.tronlib.dto.TransferStats;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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
                        mView::overviewBlockStatus,
                        t -> mView.showServerError()
                );

        TronNetwork.getInstance().getTransferStats()
                .observeOn(AndroidSchedulers.mainThread())
                .map(transferStats -> {
                    for (Stat stat : transferStats.getValue()) {
                        stat.setValue((long) (stat.getValue() / Constants.ONE_TRX));
                    }

                    return transferStats;
                })
                .subscribe(new SingleObserver<TransferStats>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TransferStats transferStats) {
                        mView.overviewTransferPastHour(transferStats.getValue());
                        mView.overviewTransactionPastHour(transferStats.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
