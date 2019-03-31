package com.devband.tronwalletforandroid.ui.sendtrc20;

import android.text.TextUtils;
import android.util.Log;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.dao.Trc20ContractDao;
import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

import org.spongycastle.util.encoders.Hex;
import org.tron.common.utils.AbiUtil;
import org.tron.core.exception.EncodingException;

import io.reactivex.Single;

public class SendTrc20Presenter extends BasePresenter<SendTrc20View> {

    private static String TRANSFER_METHOD = "transfer(address,uint256)";

    private Tron mTron;
    private Trc20ContractDao mTrc20ContractDao;
    private RxJavaSchedulers mRxJavaSchedulers;

    public SendTrc20Presenter(SendTrc20View view, Tron tron, AppDatabase appDatabase,
            RxJavaSchedulers rxJavaSchedulers) {
        super(view);
        this.mTron = tron;
        this.mTrc20ContractDao = appDatabase.trc20ContractDao();
        this.mRxJavaSchedulers = rxJavaSchedulers;
    }

    @Override
    public void onCreate() {
        loadTrc20Tokens();
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

    public void loadTrc20Tokens() {
        mView.showLoadingDialog();

        Single.fromCallable(() -> mTrc20ContractDao.getAll())
                .subscribeOn(mRxJavaSchedulers.getIo())
                .observeOn(mRxJavaSchedulers.getMainThread())
                .subscribe(trc20ContractModels -> {
                    mView.setTrc20List(trc20ContractModels);
                });
    }

    public void transferAsset(String password, String toAddress, String name, long amount) {
        if (!mTron.isLogin()) {
            mView.invalidPassword();
            return;
        }

        if (!mTron.checkPassword(password)) {
            mView.invalidPassword();
            return;
        }

        String loginAddress = mTron.getLoginAddress();

        if (TextUtils.isEmpty(loginAddress)) {
            mView.failed();
            return;
        }

        Single.fromCallable(() -> {
            String transferParams = "\"address\"," + amount;

            String contractTrigger = "";

            try {
                contractTrigger = AbiUtil.parseMethod(TRANSFER_METHOD, transferParams);
            } catch (EncodingException e ) {
                e.printStackTrace();
            }

            byte[] input = Hex.decode(contractTrigger);
            byte[] contractAddress = AccountManager.decodeFromBase58Check(toAddress);

            return mTron.callQueryContract(loginAddress, contractAddress, 0L, input, 1_000_000_000L, 0L, null);
        })
                .flatMap(result -> result)
                .subscribe(result -> {
                    mView.success();
                    Log.d(SendTrc20Activity.class.getSimpleName(), "send trc20 result : " + result);
                }, e -> {
                    e.printStackTrace();
                    mView.failed();
                });

    }
}
