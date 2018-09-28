package com.devband.tronwalletforandroid.ui.mytransfer;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Transfer;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.mytransfer.dto.TransferInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by user on 2018. 5. 17..
 */

public class TransferPresenter extends BasePresenter<TransferView> {

    private AdapterDataModel<TransferInfo> mAdapterDataModel;
    private Tron mTron;
    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public TransferPresenter(TransferView view, Tron tron, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<TransferInfo> adapterDataModel) {
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

    public void loadTransfer(long startIndex, int pageSize) {
        mView.showLoadingDialog();

        String address = mTron.getLoginAddress();

        mTronNetwork.getTransfersByAddress("-timestamp", true, pageSize, startIndex, address)
        .map(transactions -> {
            List<TransferInfo> infos = new ArrayList<>();

            for (Transfer t : transactions.getData()) {
                TransferInfo info = new TransferInfo();
                info.setHash(t.getHash());
                info.setAmount(t.getAmount());
                info.setBlock(t.getBlock());
                info.setTimestamp(t.getTimestamp());
                info.setTokenName(t.getTokenName());
                info.setSend(address.equals(t.getTransferFromAddress()));
                info.setTransferFromAddress(t.getTransferFromAddress());
                info.setTransferToAddress(t.getTransferToAddress());
                info.setConfirmed(t.isConfirmed());
                info.setTotal(transactions.getTotal());
                infos.add(info);
            }

            return infos;
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers .getMainThread())
        .subscribe(new SingleObserver<List<TransferInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<TransferInfo> transactionInfos) {
                mAdapterDataModel.addAll(transactionInfos);

                long total = 0;

                if (!transactionInfos.isEmpty()) {
                    total = transactionInfos.get(0).getTotal();
                }

                mView.transferDataLoadSuccess(total);
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    public void clearData() {
        mAdapterDataModel.clear();
    }
}
