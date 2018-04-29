package com.devband.tronwalletforandroid.ui.main;

import android.support.annotation.Nullable;

import com.devband.tronlib.Hosts;
import com.devband.tronlib.ServiceBuilder;
import com.devband.tronlib.dto.CoinMarketCap;
import com.devband.tronlib.services.CoinMarketCapService;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.tron.protos.Protocol;

import java.net.ConnectException;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends BasePresenter<MainView> {

    private CoinMarketCapService mCoinMarketCapService;

    public MainPresenter(MainView view) {
        super(view);
    }

    @Override
    public void onCreate() {
        mCoinMarketCapService = ServiceBuilder.createService(CoinMarketCapService.class, Hosts.COINMARKETCAP_API);
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

    public void getTronMarketInfo() {
        mCoinMarketCapService.getPrice("tronix")
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
    public String getLoginWalletName() {
        return Tron.getInstance(mContext).getLoginWalletName();
    }
}
