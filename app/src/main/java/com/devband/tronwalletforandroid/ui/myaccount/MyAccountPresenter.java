package com.devband.tronwalletforandroid.ui.myaccount;

import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.FavoriteTokenDao;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.database.model.FavoriteTokenModel;
import com.devband.tronwalletforandroid.database.model.Trc10AssetModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.tron.exception.InvalidPasswordException;
import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.main.dto.Frozen;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MyAccountPresenter extends BasePresenter<MyAccountView> {

    private Tron mTron;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;
    private FavoriteTokenDao mFavoriteTokenDao;

    public MyAccountPresenter(MyAccountView view, Tron tron, WalletAppManager walletAppManager,
            RxJavaSchedulers rxJavaSchedulers, AppDatabase appDatabase) {
        super(view);
        this.mTron = tron;
        this.mWalletAppManager = walletAppManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
        this.mFavoriteTokenDao = appDatabase.favoriteTokenDao();
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

    public Single<List<AccountModel>> getAccountList() {
        return mTron.getAccountList();
    }

    public void getAccountAccountInfo() {
        final String loginAddress = mTron.getLoginAddress();

        mView.showLoadingDialog();

        mTron.queryAccount(loginAddress)
                .map((account -> {
                    List<Frozen> frozenList = new ArrayList<>();

                    for (org.tron.protos.Protocol.Account.Frozen frozen : account.getFrozenList()) {
                        frozenList.add(Frozen.builder()
                                .frozenBalance(frozen.getFrozenBalance())
                                .expireTime(frozen.getExpireTime())
                                .build());
                    }

                    long accountId = mTron.getLoginAccount().getId();
                    List<Asset> assetList = new ArrayList<>();

//                        for (Trc20Token trc20TokenBalance : account.getTrc20TokenBalances()) {
//                            assetList.add(Asset.builder()
//                                    .name(trc20TokenBalance.getName())
//                                    .displayName("[TRC20] " + trc20TokenBalance.getName())
//                                    .balance(trc20TokenBalance.getBalance() / Math.pow(10, trc20TokenBalance.getDecimals()))
//                                    .build());
//                        }

                    for (String key : account.getAssetV2Map().keySet()) {
                        Trc10AssetModel trc10Asset = mTron.getTrc10Asset(key);

                        assetList.add(Asset.builder()
                                .name(key)
                                .displayName("[" + key + "]" + trc10Asset.getName())
                                .balance(trc10Asset.getPrecision() > 0 ?
                                        account.getAssetV2Map().get(key) / Math.pow(10, trc10Asset.getPrecision())
                                        : account.getAssetV2Map().get(key))
                                .build());
                    }

                    return TronAccount.builder()
                            .balance(account.getBalance())
                            .bandwidth(account.getDelegatedFrozenBalanceForBandwidth())
                            .assetList(assetList)
                            .frozenList(frozenList)
                            .build();
                }))
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<TronAccount>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(TronAccount account) {
                mView.displayAccountInfo(loginAddress, account);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                // todo - error msg
                if (e instanceof ConnectException) {
                    // internet error
                }

                mView.showServerError();
                mView.displayAccountInfo(loginAddress, null);
            }
        });
    }



    public String getLoginPrivateKey(@NonNull String password) {
        return mTron.getLoginPrivateKey(password);
    }

    public void changeLoginAccount(@NonNull AccountModel accountModel) {
        mTron.changeLoginAccount(accountModel);
    }

    public void freezeBalance(@NonNull String password, long freezeBalance) {
        mView.showLoadingDialog();

        mTron.freezeBalance(password, freezeBalance, Constants.FREEZE_DURATION)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mView.successFreezeBalance();
                } else {
                    mView.showServerError();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof InvalidPasswordException) {
                    mView.showInvalidPasswordMsg();
                } else {
                    e.printStackTrace();
                    mView.showServerError();
                }
            }
        });
    }

    public void unfreezeBalance(@NonNull String password) {
        mView.showLoadingDialog();

        mTron.unfreezeBalance(password)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    mView.successFreezeBalance();
                } else {
                    mView.showServerError();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof RuntimeException) {
                    mView.unableToUnfreeze();
                } else {
                    mView.showServerError();
                }
            }
        });
    }

    public long getLoginAccountIndex() {
        return mTron.getLoginAccount().getId();
    }

    public AccountModel getLoginAccount() {
        return mTron.getLoginAccount();
    }

    public int getAccountCount() {
        return mTron.getAccountCount();
    }

    public boolean isFavoriteToken(@NonNull String tokenName) {
        if (mTron.getLoginAccount() != null) {
            long accountId = mTron.getLoginAccount().getId();

            return mFavoriteTokenDao.findByAccountIdAndTokenName(accountId, tokenName) != null;
        }

        return false;
    }

    public void doFavorite(@NonNull String tokenName) {
        if (mTron.getLoginAccount() != null) {
            long accountId = mTron.getLoginAccount().getId();
            FavoriteTokenModel model = FavoriteTokenModel.builder()
                    .accountId(accountId)
                    .tokenName(tokenName)
                    .build();

            mFavoriteTokenDao.insert(model);
        }
    }

    public void removeFavorite(@NonNull String tokenName) {
        if (mTron.getLoginAccount() != null) {
            long accountId = mTron.getLoginAccount().getId();
            FavoriteTokenModel favoriteTokenModel = mFavoriteTokenDao.findByAccountIdAndTokenName(accountId, tokenName);
            mFavoriteTokenDao.delete(favoriteTokenModel);
        }
    }

    public boolean matchPassword(@NonNull String password) {
        return mWalletAppManager.login(password) == WalletAppManager.SUCCESS;
    }

    public void removeAccount(long accountId, String accountName) {
        mView.showLoadingDialog();
        Single.fromCallable(() -> {
            mTron.removeAccount(accountId, accountName);
            return true;
        })
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe((result) -> mView.successDelete());
    }
}
