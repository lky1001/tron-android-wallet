package com.devband.tronwalletforandroid.ui.token;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Token;
import com.devband.tronlib.dto.Tokens;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.tron.protos.Protocol;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class TokenPresenter extends BasePresenter<TokenView> {

    private AdapterDataModel<Token> mAdapterDataModel;
    private Tron mTron;
    private TronNetwork mTronNetwork;
    private WalletAppManager mWalletAppManager;
    private RxJavaSchedulers mRxJavaSchedulers;

    public TokenPresenter(TokenView view, Tron tron, TronNetwork tronNetwork, WalletAppManager walletAppManager,
            RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mTronNetwork = tronNetwork;
        this.mWalletAppManager = walletAppManager;
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    public void setAdapterDataModel(AdapterDataModel<Token> adapterDataModel) {
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

    public void clearData() {
        mAdapterDataModel.clear();
    }

    public void findToken(@NonNull String query, long startIndex, int pageSize) {
        mView.showLoadingDialog();

        Single.zip(mTron.queryAccount(mTron.getLoginAddress()), mTronNetwork.findTokens("%" + query + "%", startIndex, pageSize, "-name"),
                (account, tokens) -> {
                    AccountInfo accountInfo = new AccountInfo();
                    accountInfo.account = account;
                    accountInfo.tokens = tokens;

                    return accountInfo;
                })
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<AccountInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(AccountInfo accountInfo) {
                        mAdapterDataModel.addAll(accountInfo.tokens.getData());
                        mView.finishLoading(accountInfo.tokens.getTotal(), accountInfo.account);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void loadItems(long startIndex, int pageSize) {
        String acc = mTron.getLoginAddress();

        if (TextUtils.isEmpty(acc)) {
            mView.needLogin();
            return;
        }

        mView.showLoadingDialog();

        Single.zip(mTron.queryAccount(acc), mTronNetwork.getTokens(startIndex, pageSize, "-name", "ico"),
                (account, tokens) -> {
                    AccountInfo accountInfo = new AccountInfo();
                    accountInfo.account = account;
                    accountInfo.tokens = tokens;

                    return accountInfo;
                })
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(new SingleObserver<AccountInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(AccountInfo accountInfo) {
                        mAdapterDataModel.addAll(accountInfo.tokens.getData());
                        mView.finishLoading(accountInfo.tokens.getTotal(), accountInfo.account);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void participateToken(@NonNull String password, Token item, long tokenAmount) {
        mView.showLoadingDialog();

        mTron.participateTokens(password, item.getName(), item.getOwnerAddress(), tokenAmount)
        .subscribeOn(mRxJavaSchedulers.getIo())
        .observeOn(mRxJavaSchedulers.getMainThread())
        .subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                mView.participateTokenResult(result);
            }

            @Override
            public void onError(Throwable e) {
                mView.showServerError();
            }
        });
    }

    public boolean matchPassword(@NonNull String password) {
        return mWalletAppManager.login(password) == WalletAppManager.SUCCESS;
    }

    private class AccountInfo {
        Protocol.Account account;
        Tokens tokens;
    }
}
