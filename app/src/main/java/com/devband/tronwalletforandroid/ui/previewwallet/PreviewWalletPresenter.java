package com.devband.tronwalletforandroid.ui.previewwallet;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class PreviewWalletPresenter extends BasePresenter<PreviewWalletView> {

    private Tron mTron;
    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;
    private AdapterDataModel<TronWallet> mAdapterDataModel;

    public PreviewWalletPresenter(PreviewWalletView view, Tron tron, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<TronWallet> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        mTron.getAccountList()
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .map(accountList -> {
                    List<TronWallet> walletList = new ArrayList<>();
                    for (AccountModel accountModel : accountList) {
                        walletList.add(TronWallet.builder()
                                .accountName(accountModel.getName())
                                .address(accountModel.getAddress())
                                .build());
                    }

                    return walletList;
                })
                .subscribe(accountList -> {
                    mAdapterDataModel.clear();
                    mAdapterDataModel.addAll(accountList);
                }, e -> e.printStackTrace());
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
}
