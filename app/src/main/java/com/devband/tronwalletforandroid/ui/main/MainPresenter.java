package com.devband.tronwalletforandroid.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.tronscan.Trc20Token;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.common.Hex2Decimal;
import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.FavoriteTokenDao;
import com.devband.tronwalletforandroid.database.dao.Trc20ContractDao;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.database.model.Trc10AssetModel;
import com.devband.tronwalletforandroid.database.model.Trc20ContractModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.main.dto.Frozen;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.spongycastle.util.encoders.Hex;
import org.tron.api.GrpcAPI;
import org.tron.protos.Protocol;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainPresenter extends BasePresenter<MainView> {

    private AdapterDataModel<Asset> mAdapterDataModel;
    private Tron mTron;
    private TronNetwork mTronNetwork;
    private RxJavaSchedulers mRxJavaSchedulers;
    private CustomPreference mCustomPreference;
    private FavoriteTokenDao mFavoriteTokenDao;
    private Trc20ContractDao mTrc20ContractDao;
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
        this.mTrc20ContractDao = appDatabase.trc20ContractDao();
    }

    public void setAdapterDataModel(AdapterDataModel<Asset> adapterDataModel) {
        this.mAdapterDataModel = adapterDataModel;
    }

    @Override
    public void onCreate() {
        syncTrc20TokenContracts();
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

    public void getMyAccountTrc10Info() {
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
                                Trc10AssetModel trc10Asset = mTron.getTrc10Asset(key);

                                assetList.add(Asset.builder()
                                        .name(key)
                                        .displayName("[" + key + "]" + trc10Asset.getName())
                                        .balance(trc10Asset.getPrecision() > 0 ?
                                                account.getAssetV2Map().get(key) / Math.pow(10, trc10Asset.getPrecision())
                                                : account.getAssetV2Map().get(key))
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
                            } else if (e instanceof IllegalArgumentException) {
                                mView.goToIntroActivity();
                            }

                            mView.connectionError();
                        }
                    });
        } else {
            mView.goToIntroActivity();
        }
    }


    public void getMyAccountTrc20Info(boolean isHideNoBalance) {
        mAdapterDataModel.clear();

        mView.showLoadingDialog();
        String loginAddress = mTron.getLoginAddress();

        if (!TextUtils.isEmpty(loginAddress)) {
            Single<List<Asset>> trc20List = Single.fromCallable(() -> {
                List<Trc20ContractModel> list = mTrc20ContractDao.getAll();

                List<Asset> assetList = new ArrayList<>();

                for (Trc20ContractModel trc20ContractModel : list) {
                    GrpcAPI.TransactionExtention transactionExtention = mTron.getTrc20Balance(loginAddress, trc20ContractModel.getAddress()).blockingGet();

                    if (transactionExtention == null || !transactionExtention.getResult().getResult()) {
                        Timber.d("RPC create call trx failed!");
                        Timber.d("Code = " + transactionExtention != null ? String.valueOf(transactionExtention.getResult().getCode()) : "null");
                        Timber.d("Message = " + transactionExtention != null ? transactionExtention.getResult().getMessage().toStringUtf8() : "null");
                        continue;
                    }

                    Protocol.Transaction resultTransaction = transactionExtention.getTransaction();

                    if (resultTransaction.getRetCount() != 0 &&
                            transactionExtention.getConstantResult(0) != null &&
                            transactionExtention.getResult() != null) {
                        byte[] result = transactionExtention.getConstantResult(0).toByteArray();
//                        System.out.println("message:" + resultTransaction.getRet(0).getRet());
//                        System.out.println(":" + ByteArray
//                                .toStr(transactionExtention.getResult().getMessage().toByteArray()));
//                        System.out.println("Result:" + Hex2Decimal.hex2Decimal(Hex.toHexString(result)));

                        long balance = Hex2Decimal.hex2Decimal(Hex.toHexString(result));

                        if (isHideNoBalance && balance == 0) {
                            continue;
                        }

                        assetList.add(Asset.builder()
                                .name(trc20ContractModel.getName())
                                .displayName(trc20ContractModel.getName() + "(" + trc20ContractModel.getSymbol() + ")")
                                .balance(balance / Math.pow(10, trc20ContractModel.getPrecision()))
                                .build());
                    }
                }

                return assetList;
            });

            Single.zip(mTron.queryAccount(loginAddress), trc20List, (account, trc20) -> {
                List<Frozen> frozenList = new ArrayList<>();

                for (org.tron.protos.Protocol.Account.Frozen frozen : account.getFrozenList()) {
                    frozenList.add(Frozen.builder()
                            .frozenBalance(frozen.getFrozenBalance())
                            .expireTime(frozen.getExpireTime())
                            .build());
                }

                return TronAccount.builder()
                        .balance(account.getBalance())
                        .bandwidth(account.getDelegatedFrozenBalanceForBandwidth())
                        .assetList(trc20)
                        .frozenList(frozenList)
                        .build();
            })
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
                            } else if (e instanceof IllegalArgumentException) {
                                mView.goToIntroActivity();
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

    public void addTrc20Contract(String name, String symbol, String contractAddress, int precision) {
        mView.showLoadingDialog();

        mTron.checkTrc20Contract(contractAddress)
                .subscribeOn(mRxJavaSchedulers.getIo())
                .map(result -> {
                    if (result == Tron.SUCCESS) {
                        Trc20ContractModel trc20Contract = mTrc20ContractDao.findByContractAddress(contractAddress);

                        if (trc20Contract == null) {
                            trc20Contract = Trc20ContractModel.builder()
                                    .name(name)
                                    .symbol(symbol)
                                    .address(contractAddress)
                                    .precision(precision)
                                    .isFavorite(false)
                                    .build();

                            mTrc20ContractDao.insert(trc20Contract);
                        }
                    }

                    return result;
                })
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(result -> mView.resultAddTrc20(result), e -> {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                    mView.connectionError();
                });
    }

    public void syncTrc20TokenContracts() {
        mView.showSyncTrc20Loading();

        mTronNetwork.getTrc20TokenList()
                .subscribeOn(mRxJavaSchedulers.getIo())
                .map(trc20Tokens -> {
                    List<Trc20ContractModel> savedTokens = mTrc20ContractDao.getAll();

                    List<Trc20Token> newTokens = trc20Tokens.getTrc20TokenList();

                    List<Trc20Token> addTokens = new ArrayList<>();

                    for (Trc20Token newToken : newTokens) {
                        boolean isAdd = true;

                        for (Trc20ContractModel savedToken : savedTokens) {
                            if (newToken.getContractAddress().equalsIgnoreCase(savedToken.getAddress())) {
                                isAdd = false;
                                break;
                            }
                        }

                        if (isAdd) {
                            addTokens.add(newToken);
                        }
                    }

                    List<Trc20ContractModel> addModels = new ArrayList<>();

                    for (Trc20Token addToken : addTokens) {
                        addModels.add(Trc20ContractModel.builder()
                                .name(addToken.getName())
                                .symbol(addToken.getSymbol())
                                .address(addToken.getContractAddress())
                                .precision(addToken.getPrecision())
                                .isFavorite(false)
                                .build());
                    }

                    if (!addModels.isEmpty()) {
                        mTrc20ContractDao.insertAll(addModels);
                    }

                    return true;
                })
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(result -> {
                    mView.finishSyncTrc20();
                }, e -> {
                    e.printStackTrace();
                    mView.finishSyncTrc20();
                });
    }
}
