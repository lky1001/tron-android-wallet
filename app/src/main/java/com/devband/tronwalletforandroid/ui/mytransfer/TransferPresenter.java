package com.devband.tronwalletforandroid.ui.mytransfer;

import android.util.Log;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Transfer;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;
import com.devband.tronwalletforandroid.ui.mytransfer.dto.TransferInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 2018. 5. 17..
 */

public class TransferPresenter extends BasePresenter<TransferView> {

    public TransferPresenter(TransferView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        loadTransaction();
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

    private void loadTransaction() {
        mView.showLoadingDialog();

        String address = Tron.getInstance(mContext).getLoginAddress();

        TronNetwork.getInstance().getTransfers(address, Constants.TRON_SYMBOL)
        .subscribeOn(Schedulers.io())
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
            return infos;
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<List<TransferInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<TransferInfo> transactionInfos) {
                mView.transferDataLoadSuccess(transactionInfos);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("hanseon--", "onError : " + e.getMessage());
                mView.showServerError();
            }
        });
    }

}
