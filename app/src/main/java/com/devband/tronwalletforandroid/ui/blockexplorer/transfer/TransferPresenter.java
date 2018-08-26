package com.devband.tronwalletforandroid.ui.blockexplorer.transfer;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Transfer;
import com.devband.tronlib.dto.Transfers;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class TransferPresenter extends BasePresenter<TransferView> {

    private AdapterDataModel<Transfer> mAdapterDataModel;

    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public TransferPresenter(TransferView view, TronNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<Transfer> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
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

    public void getTransfer(long startIndex, int pageSize) {
        mView.showLoadingDialog();

        mTronNetwork
                .getTransfers(startIndex, pageSize, "-timestamp", true)
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Transfers>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Transfers transactions) {
                        mAdapterDataModel.addAll(transactions.getData());
                        mView.finishLoading(transactions.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }

    public void getTransfer(long block, long startIndex, int pageSize) {
        mView.showLoadingDialog();
        mTronNetwork
                .getTransfers("-timestamp", true, pageSize, startIndex, block)
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Transfers>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Transfers transfers) {
                        mAdapterDataModel.addAll(transfers.getData());
                        mView.finishLoading(transfers.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
