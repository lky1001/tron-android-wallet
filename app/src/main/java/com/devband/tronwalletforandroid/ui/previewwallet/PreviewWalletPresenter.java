package com.devband.tronwalletforandroid.ui.previewwallet;

import com.devband.tronlib.TronNetwork;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class PreviewWalletPresenter extends BasePresenter<PreviewWalletView> {

    private Tron mTron;
    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;
    private AdapterDataModel<AccountModel> mAdapterDataModel;

    public PreviewWalletPresenter(PreviewWalletView view, Tron tron, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<AccountModel> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        refreshAccount();
    }

    public void refreshAccount() {
        mTron.getAccountList()
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(accountList -> {
                    mAdapterDataModel.clear();
                    mAdapterDataModel.addAll(accountList);
                    mView.finishRefresh();
                }, e -> mView.finishRefresh());
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
