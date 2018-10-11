package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.RichInfo;
import com.devband.tronlib.dto.RichTotal;
import com.devband.tronlib.dto.Stat;
import com.devband.tronlib.dto.TransferStats;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

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

        mTronNetwork.getTopAddressAccounts(10)
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(
                        mView::overviewDataLoadSuccess,
                        t -> mView.showServerError()
                );

        mTronNetwork.getAvgBlockSize()
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(
                        mView::overviewAvgBlockSize,
                        t -> mView.showServerError()
                );

        mTronNetwork.getSystemStatus()
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(
                        mView::overviewBlockStatus,
                        t -> mView.showServerError()
                );

        mTronNetwork.getTransferStats()
                .observeOn(mRxJavaSchedulers.getMainThread())
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

    void richListDataLoad() {
        mView.showLoadingDialog();

        Single.zip(mTronNetwork.getRichList(), mTronNetwork.getCoinInfo(Constants.TRON_COINMARKET_NAME),
                ((richData, coinMarketCaps) -> {
                    List<RichItemViewModel> viewModels = new ArrayList<>();
                    RichTotal total = richData.getTotal();
                    for (RichInfo info : richData.getData()) {
                        viewModels.add(new RichItemViewModel(total, info, coinMarketCaps.get(0)));
                    }
                    return viewModels;
                }))
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(
                        mView::richListLoadSuccess,
                        t -> mView.showServerError()
                );
    }
}
