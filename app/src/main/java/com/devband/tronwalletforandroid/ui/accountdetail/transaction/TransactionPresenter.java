package com.devband.tronwalletforandroid.ui.accountdetail.transaction;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Transaction;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.transaction.dto.TransactionInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TransactionPresenter extends BasePresenter<TransactionView> {

    private AdapterDataModel<TransactionInfo> mAdapterDataModel;

    public TransactionPresenter(TransactionView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<TransactionInfo> adapterDataModel) {
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

    public void getTransactions(String address, int startIndex, int pageSize) {
        mView.showLoadingDialog();

        TronNetwork.getInstance()
                .getTransactions(address, startIndex, pageSize, "-timestamp", true)
                .map(transactions -> {
                    List<TransactionInfo> infos = new ArrayList<>();

                    for (Transaction t : transactions.getData()) {
                        TransactionInfo info = new TransactionInfo();
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
                .observeOn(AndroidSchedulers.mainThread())
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
        List<TransactionInfo> infos;

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public List<TransactionInfo> getInfos() {
            return infos;
        }

        public void setInfos(List<TransactionInfo> infos) {
            this.infos = infos;
        }
    }
}
