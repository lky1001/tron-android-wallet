package com.devband.tronwalletforandroid.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.FavoriteTokenDao;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
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

public class MainPresenter extends BasePresenter<MainView> {

    private AdapterDataModel<Asset> mAdapterDataModel;
    private Tron mTron;
    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;
    private CustomPreference mCustomPreference;
    private FavoriteTokenDao mFavoriteTokenDao;
    private WalletAppManager mWalletAppManager;

    public MainPresenter(MainView view, Tron tron, WalletAppManager walletAppManager, TronNetwork tronNetwork,
            RxJavaSchedulers rxJavaSchedulers, CustomPreference customPreference, AppDatabase appDatabase) {
        super(view);
        this.mTron = tron;
        this.mWalletAppManager = walletAppManager;
        this.mTronNetwork = tronNetwork;
        this.mRxJavaSchedulers = rxJavaSchedulers;
        this.mCustomPreference = customPreference;
        this.mFavoriteTokenDao = appDatabase.favoriteTokenDao();
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
        return mTron.isLogin();
    }

    public void getMyAccountInfo() {
        mView.showLoadingDialog();
        String loginAddress = mTron.getLoginAddress();

        if (!TextUtils.isEmpty(loginAddress)) {
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
                            boolean isFavorite = mCustomPreference.isFavoriteToken(accountId);

                            if (!isFavorite || (isFavorite && mFavoriteTokenDao.findByAccountIdAndTokenName(accountId, key) != null)) {
                                    assetList.add(Asset.builder()
                                            .name(key)
                                            .displayName("[" + key + "]" +mTron.getTokenName(key))
                                            .balance(account.getAssetV2Map().get(key))
                                            .build());
                            }
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
        } else {
            mView.goToIntroActivity();
        }
    }

    public void getTronMarketInfo() {
        mTronNetwork.getCoinInfo(Constants.TRON_COINMARKET_NAME)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
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
        mTron.logout();

        return true;
    }

    @Nullable
    public AccountModel getLoginAccount() {
        return mTron.getLoginAccount();
    }

    public Single<Boolean> changeLoginAccountName(@NonNull String accountName) {
        return mTron.changeLoginAccountName(accountName);
    }

    public void createAccount(@NonNull String nickname, @NonNull String password) {
        mTron.createAccount(nickname, password)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer result) {
                        if (result == Tron.SUCCESS) {
                            mView.successCreateAccount();
                        } else {
                            mView.showInvalidPasswordMsg();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public Single<List<AccountModel>> getAccountList() {
        return mTron.getAccountList();
    }

    public boolean changeLoginAccount(@NonNull AccountModel accountModel) {
        return mTron.changeLoginAccount(accountModel);
    }

    public void importAccount(@NonNull String nickname, @NonNull String privateKey, @NonNull String password) {
        mTron.importAccount(nickname, privateKey, password)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
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
                        } else if (result == Tron.ERROR_INVALID_PASSWORD) {
                            mView.showInvalidPasswordMsg();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.failCreateAccount();
                    }
                });
    }

    public long getLoginAccountIndex() {
        return mTron.getLoginAccount().getId();
    }

    public void setOnlyFavorites(boolean isFavorites) {
        if (mTron.getLoginAccount() != null) {
            mCustomPreference.setFavoriteToken(mTron.getLoginAccount().getId(), isFavorites);
        }
    }

    public boolean getIsFavoritesTokens() {
        if (mTron.getLoginAccount() != null) {
            return mCustomPreference.isFavoriteToken(mTron.getLoginAccount().getId());
        }

        return false;
    }

    public boolean matchPassword(@NonNull String password) {
        return mWalletAppManager.login(password) == WalletAppManager.SUCCESS;
    }

    public void changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        mView.showChangePasswordDialog();

        Single.fromCallable(() -> mTron.changePassword(oldPassword, newPassword))
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        mView.changePasswordResult(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.changePasswordResult(false);
                    }
                });
    }
}
