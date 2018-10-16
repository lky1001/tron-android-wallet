package com.devband.tronwalletforandroid.tron;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.dto.Account;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.exception.InvalidAddressException;
import com.devband.tronwalletforandroid.tron.exception.InvalidPasswordException;
import com.google.protobuf.ByteString;

import org.tron.api.GrpcAPI;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    private Context mContext;
    private TronNetwork mTronNetwork;
    private CustomPreference mCustomPreference;

    private List<String> mFullNodeList;

    private List<String> mSolidityNodeList;

    private ITronManager mTronManager;

    private AccountManager mAccountManager;

    private WalletAppManager mWalletAppManager;

    private boolean mFailConnectNode;

    public Tron(Context context, TronNetwork tronNetwork, CustomPreference customPreference, AccountManager accountManager,
            WalletAppManager walletAppManager) {
        this.mContext = context;
        this.mTronNetwork = tronNetwork;
        this.mCustomPreference = customPreference;
        this.mAccountManager = accountManager;
        this.mWalletAppManager = walletAppManager;
        init();
    }

    public void setFailConnectNode(boolean failCustomNode) {
        if (!TextUtils.isEmpty(mCustomPreference.getCustomFullNodeHost())) {
            this.mFailConnectNode = failCustomNode;
        }
    }

    private void init() {
        mFullNodeList = Arrays.asList(mContext.getResources().getStringArray(R.array.fullnode_ip_list));
        mSolidityNodeList = Arrays.asList(mContext.getResources().getStringArray(R.array.solidity_ip_list));
        initTronNode();
    }

    public void initTronNode() {
        if (!TextUtils.isEmpty(mCustomPreference.getCustomFullNodeHost()) && !mFailConnectNode) {
            try {
                mTronManager = new TronManager(mCustomPreference.getCustomFullNodeHost(),
                        mCustomPreference.getCustomFullNodeHost());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!mFullNodeList.isEmpty()) {
            Random random = new Random();
            int randomFullNode = random.nextInt(mFullNodeList.size());
            int randomSolidityNode = random.nextInt(mSolidityNodeList.size());

            mTronManager = new TronManager(mFullNodeList.get(randomFullNode), mSolidityNodeList.get(randomSolidityNode));
        } else {
            // exception
        }
    }

    public Single<Long> getBlockHeight() {
        return mTronManager.getBlockHeight()
                .map(block -> {
                    if (block.hasBlockHeader()) {
                        return block.getBlockHeader().getRawData().getNumber();
                    } else {
                        return 0L;
                    }
                });
    }

    public Single<Integer> createAccount(@NonNull String nickname, @NonNull String password) {
        return Single.fromCallable(() -> {
            if (!mWalletAppManager.checkPassword(password)) {
                return ERROR_INVALID_PASSWORD;
            }

            return SUCCESS;
        })
        .flatMap(result -> {
            String accountNickname = generateDefaultAccountName(nickname);

            byte[] aesKey = WalletAppManager.getEncKey(password);
            return mAccountManager.createAccount(accountNickname, aesKey);
        });
    }

    public Single<Integer> importAccount(@NonNull String nickname, @NonNull String privateKey, @NonNull String password) {
        return Single.fromCallable(() -> {
            if (!mWalletAppManager.checkPassword(password)) {
                return ERROR_INVALID_PASSWORD;
            }

            return SUCCESS;
        })
        .flatMap(result -> {
            if (result == SUCCESS) {
                String accountNickname = generateDefaultAccountName(nickname);

                byte[] aesKey = WalletAppManager.getEncKey(password);
                return mAccountManager.importAccount(accountNickname, privateKey, aesKey, true);
            } else {
                return Single.just(ERROR_INVALID_PASSWORD);
            }
        });
    }

    public void loginWithFingerPrint() {
        mWalletAppManager.loginWithFingerPrint();
        mAccountManager.loadAccountByRepository(null);
    }

    public int login(@NonNull String password) {
        int result = mWalletAppManager.login(password);

        if (result != WalletAppManager.SUCCESS) {
            return ERROR_INVALID_PASSWORD;
        }

        mAccountManager.loadAccountByRepository(null);

        return SUCCESS;
    }

    public boolean isLogin() {
        return mWalletAppManager.isLoginState();
    }

    @Nullable
    public String getLoginAddress() {
        if (!mWalletAppManager.isLoginState()) {
            return null;
        }

        return mAccountManager.getLoginAddress();
    }

    @Nullable
    public String getLoginPrivateKey(@NonNull String password) {
        if (!mWalletAppManager.checkPassword(password)) {
            return null;
        }

        byte[] encKey = WalletAppManager.getEncKey(password);

        if (encKey == null) {
            return null;
        }

        return getLoginPrivateKey(encKey);
    }

    @Nullable
    public String getLoginPrivateKey(@NonNull byte[] aesKey) {
        if (!mWalletAppManager.isLoginState()) {
            return null;
        }

        return mAccountManager.getLoginPrivateKey(aesKey);
    }

    public Single<Account> getAccount(@NonNull String address) {
        return mTronNetwork.getAccount(address);
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

    public void logout() {
        if (mAccountManager != null) {
            mAccountManager.logout();
        }

        mAccountManager = null;
    }

    public Single<Boolean> hasAccount() {
        return Single.fromCallable(() -> mAccountManager.getAccountCount() > 0);
    }

    @Nullable
    public AccountModel getLoginAccount() {
        return mAccountManager.getLoginAccount();
    }

    public Single<Boolean> changeLoginAccountName(@NonNull String accountName) {
        return mAccountManager.changeLoginAccountName(accountName);
    }

    public Single<List<AccountModel>> getAccountList() {
        return mAccountManager.getAccountList();
    }

    public boolean changeLoginAccount(@NonNull AccountModel accountModel) {
        mAccountManager.changeLoginAccount(accountModel);
        return true;
    }

    private String generateDefaultAccountName(String prefix) {
        return prefix + (mAccountManager.getAccountCount() + 1);
    }

    public Single<GrpcAPI.WitnessList> getWitnessList() {
        return mTronManager.listWitnesses();
    }

    public Single<GrpcAPI.NodeList> getNodeList() {
        return mTronManager.listNodes();
    }

    public Single<Boolean> participateTokens(@Nullable String password, String tokenName, String issuerAddress, long amount) {
        if (!mWalletAppManager.checkPassword(password)) {
            return Single.just(false);
        }

        return Single.fromCallable(() -> {
            byte[] toAddressBytes = AccountManager.decodeFromBase58Check(issuerAddress);
            byte[] ownerAddressBytes = AccountManager.decodeFromBase58Check(mAccountManager.getLoginAddress());

            ByteString bsTo = ByteString.copyFrom(toAddressBytes);
            ByteString bsName = ByteString.copyFrom(tokenName.getBytes());
            ByteString bsOwner = ByteString.copyFrom(ownerAddressBytes);

            return Contract.ParticipateAssetIssueContract
                    .newBuilder()
                    .setToAddress(bsTo)
                    .setAssetName(bsName)
                    .setOwnerAddress(bsOwner)
                    .setAmount(amount)
                    .build();
        })
        .flatMap(contract -> mTronManager.createTransaction(contract))
        .flatMap(transactionExtention-> {
            if (!transactionExtention.getResult().getResult()) {
                throw new RuntimeException();
            }

            if (transactionExtention.getTransaction().getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            Protocol.Transaction transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transactionExtention.getTransaction());
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> voteWitness(@Nullable String password, Map<String, String> witness) {
        if (!mWalletAppManager.checkPassword(password)) {
            return Single.just(false);
        }

        return Single.fromCallable(() -> {
            byte[] ownerAddressBytes = AccountManager.decodeFromBase58Check(mAccountManager.getLoginAddress());

            Contract.VoteWitnessContract.Builder builder = Contract.VoteWitnessContract.newBuilder();
            builder.setOwnerAddress(ByteString.copyFrom(ownerAddressBytes));

            for (String addressBase58 : witness.keySet()) {
                String value = witness.get(addressBase58);
                long count = Long.parseLong(value);
                Contract.VoteWitnessContract.Vote.Builder voteBuilder = Contract.VoteWitnessContract.Vote
                        .newBuilder();
                byte[] address = AccountManager.decodeFromBase58Check(addressBase58);
                if (address == null) {
                    continue;
                }
                voteBuilder.setVoteAddress(ByteString.copyFrom(address));
                voteBuilder.setVoteCount(count);
                builder.addVotes(voteBuilder.build());
            }

            return builder.build();
        })
        .flatMap(contract -> mTronManager.createTransaction(contract))
        .flatMap(transactionExtention-> {
            if (!transactionExtention.getResult().getResult()) {
                throw new RuntimeException();
            }

            if (transactionExtention.getTransaction().getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            Protocol.Transaction transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transactionExtention.getTransaction());
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> freezeBalance(@Nullable String password, long freezeBalance, long freezeDuration) {
        return Single.fromCallable(() -> {
            if (!mWalletAppManager.checkPassword(password)) {
                throw new InvalidPasswordException();
            }

            byte[] ownerAddressBytes = AccountManager.decodeFromBase58Check(mAccountManager.getLoginAddress());
            ByteString byteAddress = ByteString.copyFrom(ownerAddressBytes);

            return Contract.FreezeBalanceContract.newBuilder()
                    .setFrozenBalance(freezeBalance)
                    .setFrozenDuration(freezeDuration)
                    .setOwnerAddress(byteAddress)
                    .build();
        })
        .flatMap(contract -> mTronManager.createTransaction(contract))
        .flatMap(transactionExtention-> {
            if (!transactionExtention.getResult().getResult()) {
                throw new RuntimeException();
            }

            if (transactionExtention.getTransaction().getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            Protocol.Transaction transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transactionExtention.getTransaction());
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> unfreezeBalance(@Nullable String password) {
        if (!mWalletAppManager.checkPassword(password)) {
            return Single.just(false);
        }

        return Single.fromCallable(() -> {
            byte[] ownerAddressBytes = AccountManager.decodeFromBase58Check(mAccountManager.getLoginAddress());
            ByteString byteAddress = ByteString.copyFrom(ownerAddressBytes);

            return Contract.UnfreezeBalanceContract
                    .newBuilder()
                    .setOwnerAddress(byteAddress)
                    .build();
        })
        .flatMap(contract -> mTronManager.createTransaction(contract))
        .flatMap(transactionExtention-> {
            if (!transactionExtention.getResult().getResult()) {
                throw new RuntimeException();
            }

            if (transactionExtention.getTransaction().getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            Protocol.Transaction transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transactionExtention.getTransaction());
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> sendCoin(@NonNull String password, @NonNull String toAddress, long amount) {
        return Single.fromCallable(() -> {
            byte[] toAddressBytes = AccountManager.decodeFromBase58Check(toAddress);

            if (toAddressBytes == null) {
                throw new InvalidAddressException();
            }

            if (!mWalletAppManager.checkPassword(password)) {
                throw new InvalidPasswordException();
            }

            byte[] ownerAddressBytes = AccountManager.decodeFromBase58Check(mAccountManager.getLoginAddress());

            ByteString bsTo = ByteString.copyFrom(toAddressBytes);
            ByteString bsOwner = ByteString.copyFrom(ownerAddressBytes);

            return Contract.TransferContract.newBuilder()
                    .setToAddress(bsTo)
                    .setOwnerAddress(bsOwner)
                    .setAmount(amount)
                    .build();
        })
        .flatMap(contract -> mTronManager.createTransaction(contract))
        .flatMap(transactionExtention-> {
            if (!transactionExtention.getResult().getResult()) {
                throw new RuntimeException();
            }

            if (transactionExtention.getTransaction().getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            Protocol.Transaction transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transactionExtention.getTransaction());
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> transferAsset(@Nullable String password, String toAddress, String assetName, long amount) {
        return Single.fromCallable(() -> {
            byte[] toAddressBytes = AccountManager.decodeFromBase58Check(toAddress);

            if (toAddressBytes == null) {
                throw new InvalidAddressException();
            }

            if (!mWalletAppManager.checkPassword(password)) {
                throw new InvalidPasswordException();
            }

            byte[] ownerAddressBytes = AccountManager.decodeFromBase58Check(mAccountManager.getLoginAddress());

            ByteString bsTo = ByteString.copyFrom(toAddressBytes);
            ByteString bsName = ByteString.copyFrom(assetName.getBytes());
            ByteString bsOwner = ByteString.copyFrom(ownerAddressBytes);

            return Contract.TransferAssetContract.newBuilder()
                    .setToAddress(bsTo)
                    .setAssetName(bsName)
                    .setOwnerAddress(bsOwner)
                    .setAmount(amount)
                    .build();
        })
        .flatMap(contract -> mTronManager.createTransaction(contract))
        .flatMap(transactionExtention-> {
            if (!transactionExtention.getResult().getResult()) {
                throw new RuntimeException();
            }

            if (transactionExtention.getTransaction().getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            Protocol.Transaction transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transactionExtention.getTransaction());
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public boolean changePassword(String newPassword) {
        return false;
    }

    public Single<Integer> createWallet(String password) {
        return Single.just(mWalletAppManager.createWallet(password));
    }

    public void agreeTerms(boolean agree) {
        mWalletAppManager.agreeTerms(agree);
    }

    public void migrationOldData(@NonNull String password) {
        if (mWalletAppManager.migrationPassword(password)) {
            mAccountManager.migrationAccount(password);
            mCustomPreference.setInitWallet(true);
            mCustomPreference.setKeyStoreVersion(Build.VERSION.SDK_INT);
        }
    }
}
