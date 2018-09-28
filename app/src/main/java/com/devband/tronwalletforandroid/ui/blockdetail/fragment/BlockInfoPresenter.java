package com.devband.tronwalletforandroid.ui.blockdetail.fragment;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Blocks;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class BlockInfoPresenter extends BasePresenter<BlockInfoView> {

    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public BlockInfoPresenter(BlockInfoView view, TronNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
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

    public void getBlock(long blockNumber) {
        mView.showLoadingDialog();
        mTronNetwork.getBlock(blockNumber)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Blocks>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Blocks blocks) {
                        mView.finishLoading(blocks.getData().get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
