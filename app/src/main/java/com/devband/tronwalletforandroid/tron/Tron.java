package com.devband.tronwalletforandroid.tron;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.R;

import org.tron.api.GrpcAPI;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;

public class Tron {

    public static final int SUCCESS = 1;
    public static final int ERROR_INVALID_PASSWORD = -1;
    public static final int ERROR_PRIVATE_KEY = -2;
    public static final int ERROR_ACCESS_STORAGE = -3;
    public static final int ERROR_WALLET_DOES_NOT_EXIST = -4;
    public static final int ERROR_NEED_LOGIN = -5;
    public static final int ERROR_LOGIN = -6;

    public static final int MIN_PASSWORD_LENGTH = 8;

    private static Tron instance;

    private Context mContext;

    private List<String> mFullNodeList;

    private ITronManager mTronManager;

    private WalletManager mWalletManager;

    public static Tron getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (Tron.class) {
                if (instance == null) {
                    instance = new Tron(context);
                }
            }
        }
        return instance;
    }

    private Tron() {}

    private Tron(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mFullNodeList = Arrays.asList(mContext.getResources().getStringArray(R.array.fullnode_ip_list));

        // todo - fail over

        if (!mFullNodeList.isEmpty()) {
            mTronManager = new TronManager(mFullNodeList.get(0));
        } else {
            // exception
        }
    }

    public int registerWaller(@NonNull String password) {
        if (!mWalletManager.passwordValid(password)) {
            return ERROR_INVALID_PASSWORD;
        }

        mWalletManager = new WalletManager(true);
        return mWalletManager.store(password);
    }

    public int login(String password) {
        if (!WalletManager.passwordValid(password)) {
            return ERROR_INVALID_PASSWORD;
        }

        if (mWalletManager == null) {
            mWalletManager = new WalletManager();

            int loadWalletResult = mWalletManager.loadWalletByStorage(password);

            if (loadWalletResult == ERROR_WALLET_DOES_NOT_EXIST
                    || loadWalletResult == ERROR_INVALID_PASSWORD) {
                mWalletManager = null;
                return ERROR_WALLET_DOES_NOT_EXIST;
            }
        }

        return mWalletManager.login(password) ? SUCCESS : ERROR_INVALID_PASSWORD;
    }

    public boolean isLogin() {
        if (mWalletManager == null) {
            return false;
        }

        return mWalletManager.isLoginState();
    }

    @Nullable
    public String getAddress() {
        if (!checkWalletLogin()) {
            return null;
        }

        return mWalletManager.getAddress();
    }

    @Nullable
    public String getPrivateKey() {
        if (!checkWalletLogin()) {
            return null;
        }

        return mWalletManager.getPrivateKey();
    }

    private boolean checkWalletLogin() {
        if (mWalletManager == null || !mWalletManager.isLoginState()) {
            return false;
        }

        return true;
    }

    public Single<Protocol.Account> queryAccount(@NonNull String address) {
        if (!TextUtils.isEmpty(address)) {
            byte[] addressBytes = ByteArray.fromHexString(address);
            return mTronManager.queryAccount(addressBytes);
        } else {
            throw new IllegalArgumentException("address is required.");
        }
    }

    public Single<GrpcAPI.AccountList> listAccounts() {
        return mTronManager.listAccounts();
    }

    public Single<GrpcAPI.WitnessList> listWitnesses() {
        return mTronManager.listWitnesses();
    }

    public Single<GrpcAPI.AssetIssueList> getAssetIssueList() {
        return mTronManager.getAssetIssueList();
    }

    public Single<GrpcAPI.NodeList> listNodes() {
        return mTronManager.listNodes();
    }

    public Single<GrpcAPI.AssetIssueList> getAssetIssueByAccount(@NonNull String address) {
        if (!TextUtils.isEmpty(address)) {
            byte[] addressBytes = ByteArray.fromHexString(address);
            return mTronManager.getAssetIssueByAccount(addressBytes);
        } else {
            throw new IllegalArgumentException("address is required.");
        }
    }

    public void shutdown() {
        try {
            mTronManager.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
