package com.devband.tronwalletforandroid.ui.sendcoin;

import android.util.Log;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.tron.protos.Protocol;

import java.net.ConnectException;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SendCoinPresenter extends BasePresenter<SendCoinView> {

    public SendCoinPresenter(SendCoinView view) {
        super(view);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
        Tron.getInstance(mContext).queryAccount(Tron.getInstance(mContext).getAddress())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Protocol.Account>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Protocol.Account account) {
                        mView.displayAccountInfo(account);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        // todo - error msg
                        if (e instanceof ConnectException) {
                            // internet error
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {

    }

    public void sendCoin(String password, String toAddress, long amount) {
        if (!Tron.getInstance(mContext).isLogin() || !Tron.getInstance(mContext).validPassword(password)) {
            mView.invalidPassword();
            return;
        }

        Tron.getInstance(mContext).sendCoin(password, toAddress, amount)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Boolean>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean result) {
                Log.i(SendCoinPresenter.class.getSimpleName(), "send result : " + result);
                mView.sendCoinResult(result);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.invalidAddress();
            }
        });
    }

    public void transferAsset(String password, String toAddress, String assetName, long amount) {
        if (!Tron.getInstance(mContext).isLogin() || !Tron.getInstance(mContext).validPassword(password)) {
            mView.invalidPassword();
            return;
        }

        Tron.getInstance(mContext).transferAsset(password, toAddress, assetName, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        Log.i(SendCoinPresenter.class.getSimpleName(), "send result : " + result);
                        mView.sendCoinResult(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.invalidAddress();
                    }
                });
    }
}
