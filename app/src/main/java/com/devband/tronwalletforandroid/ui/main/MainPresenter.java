package com.devband.tronwalletforandroid.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.main.dto.Frozen;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.tron.protos.Protocol;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainView> {

    private AdapterDataModel<Asset> mAdapterDataModel;

    public MainPresenter(MainView view) {
        super(view);
    }

    public void setAdapterDataModel(AdapterDataModel<Asset> adapterDataModel) {
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

    public boolean isLogin() {
        return Tron.getInstance(mContext).isLogin();
    }

    public void getMyAccountInfo() {
        Tron.getInstance(mContext).queryAccount(Tron.getInstance(mContext).getLoginAddress())
        .subscribeOn(Schedulers.io())
        .map((account -> {
            List<Frozen> frozenList = new ArrayList<>();

            for (Protocol.Account.Frozen frozen : account.getFrozenList()) {
                frozenList.add(Frozen.builder()
                        .frozenBalance(frozen.getFrozenBalance())
                        .expireTime(frozen.getExpireTime())
                        .build());
            }

            List<Asset> assetList = new ArrayList<>();

            for (String s : account.getAssetMap().keySet()) {
                assetList.add(Asset.builder()
                        .name(s)
                        .balance(account.getAssetMap().get(s))
                        .build());
            }

            return TronAccount.builder()
                    .balance(account.getBalance())
                    .bandwidth(account.getBandwidth())
                    .assetList(assetList)
                    .frozenList(frozenList)
                    .build();
        }))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<TronAccount>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(TronAccount account) {
                mView.displayAccountInfo(account);
                mAdapterDataModel.clear();
                mAdapterDataModel.addAll(account.getAssetList());
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                // todo - error msg
                if (e instanceof ConnectException) {
                    // internet error
                }

                mView.connectionError();
            }
        });
    }

    public void getTronMarketInfo() {
        TronNetwork.getInstance().getCoinInfo(Constants.TRON_COINMARKET_NAME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CoinMarketCap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CoinMarketCap> coinMarketCaps) {
                        if (coinMarketCaps.size() > 0) {
                            mView.setTronMarketInfo(coinMarketCaps.get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public boolean logout() {
        Tron.getInstance(mContext).logout();

        return true;
    }

    @Nullable
    public AccountModel getLoginAccount() {
        return Tron.getInstance(mContext).getLoginAccount();
    }

    public Single<Boolean> changeLoginAccountName(@NonNull String accountName) {
        return Tron.getInstance(mContext).changeLoginAccountName(accountName);
    }

    public void createAccount(@NonNull String nickname) {
        Tron.getInstance(mContext).createAccount(nickname);
        mView.successCreateAccount();
    }

    public Single<List<AccountModel>> getAccountList() {
        return Tron.getInstance(mContext).getAccountList();
    }

    public void changeLoginAccount(@NonNull AccountModel accountModel) {
        Tron.getInstance(mContext).changeLoginAccount(accountModel);
    }

    public void importAccount(@NonNull String nickname, @NonNull String privateKey) {
        Tron.getInstance(mContext).importAccount(nickname, privateKey)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Integer result) {
                if (result == Tron.SUCCESS) {
                    mView.successImportAccount();
                } else if (result == Tron.ERROR_EXIST_ACCOUNT) {
                    mView.duplicatedAccount();
                } else if (result == Tron.ERROR_PRIVATE_KEY) {
                    mView.failCreateAccount();
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.failCreateAccount();
            }
        });
    }

    public int getLoginAccountIndex() {
        return Tron.getInstance(mContext).getLoginAccount().getId();
    }
}
