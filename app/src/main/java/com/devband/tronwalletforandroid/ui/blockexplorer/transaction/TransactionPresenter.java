package com.devband.tronwalletforandroid.ui.blockexplorer.transaction;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Transaction;
import com.devband.tronlib.dto.Transactions;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class TransactionPresenter extends BasePresenter<TransactionView> {

    private AdapterDataModel<Transaction> mAdapterDataModel;

    public TransactionPresenter(TransactionView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<Transaction> adapterDataModel) {
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

    public void getTransactions(int startIndex, int pageSize) {
        mView.showLoadingDialog();

        TronNetwork.getInstance()
                .getTransactions(startIndex, pageSize, "-timestamp", true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Transactions>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Transactions transactions) {
                        mAdapterDataModel.addAll(transactions.getData());
                        mView.finishLoading(transactions.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
