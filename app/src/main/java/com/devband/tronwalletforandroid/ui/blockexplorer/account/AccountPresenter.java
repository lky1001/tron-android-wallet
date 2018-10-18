package com.devband.tronwalletforandroid.ui.blockexplorer.account;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.TronAccount;
import com.devband.tronlib.dto.TronAccounts;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class AccountPresenter extends BasePresenter<AccountView> {

    private AdapterDataModel<TronAccount> mAdapterDataModel;
    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;

    public AccountPresenter(AccountView view, TronNetwork tronNetwork, RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<TronAccount> adapterDataModel) {
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

    public void getTronAccounts(long startIndex, int pageSize) {
        mView.showLoadingDialog();

        Single.zip(mTronNetwork.getAccounts(startIndex, pageSize, "-balance"), mTronNetwork.getCoinInfo(Constants.TRON_COINMARKET_NAME),
                ((tronAccounts, coinMarketCaps) -> {
                    for (TronAccount tronAccount : tronAccounts.getData()) {
                        tronAccount.setTotalSupply((long) Double.parseDouble(coinMarketCaps.get(0).getTotalSupply()));
                        tronAccount.setAvailableSypply((long) Double.parseDouble(coinMarketCaps.get(0).getAvailableSupply()));
                        tronAccount.setBalancePercent(((double) tronAccount.getBalance() / Constants.ONE_TRX / (double) tronAccount.getAvailableSypply()) * 100f);
                    }

                    return tronAccounts;
                }))
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<TronAccounts>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(TronAccounts tronAccounts) {
                        mAdapterDataModel.addAll(tronAccounts.getData());
                        mView.finishLoading(tronAccounts.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showServerError();
                    }
                });
    }
}
