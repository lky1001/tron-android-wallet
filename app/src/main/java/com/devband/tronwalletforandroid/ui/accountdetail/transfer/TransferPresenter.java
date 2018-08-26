package com.devband.tronwalletforandroid.ui.accountdetail.transfer;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Transfer;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.mytransfer.dto.TransferInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class TransferPresenter extends BasePresenter<TransferView> {

    private AdapterDataModel<TransferInfo> mAdapterDataModel;
    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public TransferPresenter(TransferView view, TronNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
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

    public void getTransfers(String address, long startIndex, int pageSize) {
        mView.showLoadingDialog();

        mTronNetwork
        .getTransfers(address, startIndex, pageSize, "-timestamp", true)
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
                infos.add(info);
            }

            Transactions info = new Transactions();
            info.setInfos(infos);
            info.setTotal(transactions.getTotal());

            return info;
        })
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Transactions>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Transactions transactionInfos) {
                mAdapterDataModel.addAll(transactionInfos.getInfos());
                mView.finishLoading(transactionInfos.total);
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    class Transactions {
        long total;
        List<TransferInfo> infos;

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public List<TransferInfo> getInfos() {
            return infos;
        }

        public void setInfos(List<TransferInfo> infos) {
            this.infos = infos;
        }
    }
}
