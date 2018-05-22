package com.devband.tronwalletforandroid.tron;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.exception.InvalidAddressException;
import com.devband.tronwalletforandroid.tron.exception.InvalidPasswordException;

import org.tron.api.GrpcAPI;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

public class Tron {

    public static final int SUCCESS = 1;
    public static final int ERROR_INVALID_PASSWORD = -1;
    public static final int ERROR_PRIVATE_KEY = -2;
    public static final int ERROR_ACCESS_STORAGE = -3;
    public static final int ERROR_ACCOUNT_DOES_NOT_EXIST = -4;
    public static final int ERROR_NEED_LOGIN = -5;
    public static final int ERROR_LOGIN = -6;
    public static final int ERROR_EXIST_ACCOUNT = -7;
    public static final int ERROR = -9999;

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int PRIVATE_KEY_SIZE = 64;

    private static Tron instance;

    private Context mContext;

    private List<String> mFullNodeList;

    private ITronManager mTronManager;

    private AccountManager mAccountManager;

    public static synchronized Tron getInstance(@NonNull Context context) {
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
        initTronNode();

        mAccountManager = new AccountManager(AccountManager.ACCOUNT_LOCAL_DB, mContext);
    }

    public void initTronNode() {
        // todo - fail over

        if (!TextUtils.isEmpty(CustomPreference.getInstance(mContext).getCustomFullNodeHost())) {
            mTronManager = new TronManager(CustomPreference.getInstance(mContext).getCustomFullNodeHost());
        } else if (!mFullNodeList.isEmpty()) {
            mTronManager = new TronManager(mFullNodeList.get(0));
        } else {
            // exception
        }
    }

    public int registerAccount(@NonNull String nickname, @NonNull String password) {
        if (!AccountManager.passwordValid(password)) {
            return ERROR_INVALID_PASSWORD;
        }

        if (mAccountManager == null) {
            mAccountManager = new AccountManager(true, mContext);
        }

        return mAccountManager.genAccount(generateDefaultAccountName(nickname), password);
    }

    public int importAccount(@NonNull String nickname, @NonNull String privateKey) {
        return mAccountManager.importAccount(generateDefaultAccountName(nickname), privateKey);
    }

    public int login(String password) {
        if (!AccountManager.passwordValid(password)) {
            return ERROR_INVALID_PASSWORD;
        }

        if (mAccountManager == null) {
            mAccountManager = new AccountManager(AccountManager.ACCOUNT_LOCAL_DB, mContext);
        }

        if (!mAccountManager.login(password)) {
            return ERROR_INVALID_PASSWORD;
        }

        return SUCCESS;
    }

    public boolean isLogin() {
        if (mAccountManager == null) {
            return false;
        }

        return mAccountManager.isLoginState();
    }

    @Nullable
    public String getLoginAddress() {
        if (!checkAccountLogin()) {
            return null;
        }

        return mAccountManager.getLoginAddress();
    }

    @Nullable
    public String getLoginPrivateKey() {
        if (!checkAccountLogin()) {
            return null;
        }

        return mAccountManager.getLoginPrivateKey();
    }

    private boolean checkAccountLogin() {
        if (mAccountManager == null || !mAccountManager.isLoginState()) {
            return false;
        }

        return true;
    }

    public Single<Protocol.Account> queryAccount(@NonNull String address) {
        if (!TextUtils.isEmpty(address)) {
            byte[] addressBytes = AccountManager.decodeFromBase58Check(address);
            if (addressBytes == null) {
                throw new IllegalArgumentException("Invalid address.");
            }

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

    public Single<Boolean> sendCoin(@NonNull String password, @NonNull String toAddress, long amount) {
        return Single.fromCallable(() -> {
            byte[] toAddressBytes = AccountManager.decodeFromBase58Check(toAddress);

            if (toAddressBytes == null) {
                throw new InvalidAddressException();
            }

            if (!mAccountManager.checkPassWord(password)) {
                throw new InvalidPasswordException();
            }

            Contract.TransferContract contract = mAccountManager.createTransferContract(toAddressBytes, amount);

            return mTronManager.createTransaction(contract);
        })
        .flatMap(transactionSingle -> {
            Protocol.Transaction transaction = transactionSingle.blockingGet();

            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> transferAsset(String password, String toAddress, String assetName, long amount) {
        return Single.fromCallable(() -> {
            byte[] toAddressBytes = AccountManager.decodeFromBase58Check(toAddress);

            if (toAddressBytes == null) {
                throw new InvalidAddressException();
            }

            if (!mAccountManager.checkPassWord(password)) {
                throw new InvalidPasswordException();
            }

            Contract.TransferAssetContract contract = mAccountManager.createTransferAssetTransaction(toAddressBytes, assetName.getBytes(), amount);

            return mTronManager.createTransferAssetTransaction(contract);
        })
        .flatMap(transactionSingle -> {
            Protocol.Transaction transaction = transactionSingle.blockingGet();

            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public void shutdown() {
        try {
            mTronManager.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean validPassword(String password) {
        if (mAccountManager == null) {
            return false;
        }

        if (!AccountManager.passwordValid(password)) {
            return false;
        }

        return mAccountManager.checkPassWord(password);
    }

    public void logout() {
        mAccountManager.logout();
        mAccountManager = null;
    }

    public boolean hasAccount() {
        return mAccountManager.getAccountCount() > 0;
    }

    @Nullable
    public AccountModel getLoginAccount() {
        return mAccountManager.getLoginAccount();
    }

    public boolean changeLoginAccountName(@NonNull String accountName) {
        mAccountManager.changeLoginAccountName(accountName);
        return true;
    }

    public void createAccount(@NonNull String nickname) {
        mAccountManager.createAccount(generateDefaultAccountName(nickname));
    }

    public List<AccountModel> getAccountList() {
        return mAccountManager.getAccountList();
    }

    public void changeLoginAccount(@NonNull AccountModel accountModel) {
        mAccountManager.changeLoginAccount(accountModel);
    }

    private String generateDefaultAccountName(String prefix) {
        int cnt = mAccountManager.getAccountCount();
        return prefix + (++cnt);
    }

    public Single<GrpcAPI.WitnessList> getWitnessList() {
        return mTronManager.listWitnesses();
    }

    public Single<GrpcAPI.NodeList> getNodeList() {
        return mTronManager.listNodes();
    }

    public Single<GrpcAPI.AccountList> getTronAccountList() {
        return mTronManager.listAccounts();
    }

    public Single<Boolean> voteWitness(Map<String, String> witness) {
        return Single.fromCallable(() -> {
            Contract.VoteWitnessContract voteWitnessContract = mAccountManager.createVoteWitnessContract(witness);

            return mTronManager.createTransaction(voteWitnessContract);
        })
        .flatMap(transactionSingle -> {
            Protocol.Transaction transaction = transactionSingle.blockingGet();

            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> freezeBalance(long freezeBalance, long freezeDuration) {
        return Single.fromCallable(() -> {
            Contract.FreezeBalanceContract freezeBalanceContract = mAccountManager.createFreezeBalanceContract(freezeBalance, freezeDuration);

            return mTronManager.createTransaction(freezeBalanceContract);
        })
        .flatMap(transactionSingle -> {
            Protocol.Transaction transaction = transactionSingle.blockingGet();

            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> unfreezeBalance() {
        return Single.fromCallable(() -> {
            Contract.UnfreezeBalanceContract unfreezeBalanceContract = mAccountManager.createUnfreezeBalanceContract();

            return mTronManager.createTransaction(unfreezeBalanceContract);
        })
        .flatMap(transactionSingle -> {
            Protocol.Transaction transaction = transactionSingle.blockingGet();

            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }
}
