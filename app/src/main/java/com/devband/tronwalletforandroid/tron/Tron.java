package com.devband.tronwalletforandroid.tron;

import android.content.Context;
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

    public Single<Integer> registerAccount(@NonNull String nickname, @NonNull String privateKey, @NonNull String password) {
        return Single.fromCallable(() -> {
            if (!WalletAppManager.passwordValid(password)) {
                return ERROR_INVALID_PASSWORD;
            }

            return SUCCESS;
        })
        .flatMap(result -> {
            if (result == SUCCESS) {
                return generateDefaultAccountName(nickname);
            } else {
                return null;
            }
        })
        .flatMap(name -> {
            if (name != null) {
                byte[] aesKey = WalletAppManager.getEncKey(password);
                if (aesKey != null) {
                    return mAccountManager.importAccount(name, privateKey, aesKey, false);
                }
            }

            return Single.just(ERROR_INVALID_PASSWORD);
        });

    }

    public Single<Integer> registerAccount(@NonNull String nickname, @NonNull String password) {
        return Single.fromCallable(() -> {
            if (!WalletAppManager.passwordValid(password)) {
                return ERROR_INVALID_PASSWORD;
            }

            return SUCCESS;
        })
        .flatMap(result -> {
            if (result == SUCCESS) {
                return generateDefaultAccountName(nickname);
            } else {
                return null;
            }
        })
        .flatMap(name -> {
            if (name != null) {
                byte[] aesKey = WalletAppManager.getEncKey(password);
                return mAccountManager.genAccount(name, aesKey);
            }

            return Single.just(ERROR_INVALID_PASSWORD);
        });
    }

    public Single<Integer> importAccount(@NonNull String nickname, @NonNull String privateKey, @NonNull String password) {
        return generateDefaultAccountName(nickname)
            .flatMap(name -> {
                byte[] aesKey = WalletAppManager.getEncKey(password);
                return mAccountManager.importAccount(name, privateKey, aesKey, true);
            });
    }

    public int login(String password) {
        if (!WalletAppManager.passwordValid(password)) {
            return ERROR_INVALID_PASSWORD;
        }

        byte[] aesKey = WalletAppManager.getEncKey(password);

        if (aesKey == null || mAccountManager.loadAccountByRepository(null, aesKey) != Tron.SUCCESS) {
            return ERROR_INVALID_PASSWORD;
        }

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
        if (!mWalletAppManager.isLoginState()) {
            return null;
        }

        byte[] encKey = WalletAppManager.getEncKey(password);

        if (encKey == null) {
            return null;
        }

        return mAccountManager.getLoginPrivateKey(encKey);
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

    public Single<Boolean> sendCoin(@NonNull String password, @NonNull String toAddress, long amount) {
        return Single.fromCallable(() -> {
            byte[] toAddressBytes = AccountManager.decodeFromBase58Check(toAddress);

            if (toAddressBytes == null) {
                throw new InvalidAddressException();
            }

            if (!mWalletAppManager.checkPassWord(password)) {
                throw new InvalidPasswordException();
            }

            Contract.TransferContract contract = mAccountManager.createTransferContract(WalletAppManager.getEncKey(password), toAddressBytes, amount);

            return mTronManager.createTransaction(contract);
        })
        .flatMap(transactionSingle -> transactionSingle)
        .flatMap(transaction -> {
            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> transferAsset(@Nullable String password, String toAddress, String assetName, long amount) {
        return Single.fromCallable(() -> {
            byte[] toAddressBytes = AccountManager.decodeFromBase58Check(toAddress);

            if (toAddressBytes == null) {
                throw new InvalidAddressException();
            }

            if (!mWalletAppManager.checkPassWord(password)) {
                throw new InvalidPasswordException();
            }

            Contract.TransferAssetContract contract = mAccountManager.createTransferAssetTransaction(WalletAppManager.getEncKey(password), toAddressBytes, assetName.getBytes(), amount);

            return mTronManager.createTransferAssetTransaction(contract);
        })
        .flatMap(transactionSingle -> transactionSingle)
        .flatMap(transaction -> {
            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transaction);
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

    public boolean validPassword(@Nullable String password) {
        if (!WalletAppManager.passwordValid(password)) {
            return false;
        }

        return mWalletAppManager.checkPassWord(password);
    }

    public void logout() {
        if (mAccountManager != null) {
            mAccountManager.logout();
        }

        mAccountManager = null;
    }

    public Single<Boolean> hasAccount() {
        return mAccountManager.getAccountCount()
                .flatMap(count -> {
                    if (count > 0) {
                        return Single.just(true);
                    } else {
                        return Single.just(false);
                    }
                });
    }

    @Nullable
    public AccountModel getLoginAccount() {
        return mAccountManager.getLoginAccount();
    }

    public Single<Boolean> changeLoginAccountName(@NonNull String accountName) {
        return mAccountManager.changeLoginAccountName(accountName);
    }

    public Single<Boolean> createAccount(@NonNull String nickname, @NonNull String password) {
        return generateDefaultAccountName(nickname)
                .flatMap(name -> {
                    byte[] aesKey = WalletAppManager.getEncKey(password);
                    return mAccountManager.createAccount(name, aesKey);
                })
                .flatMap(result -> {
                    if (result == Tron.SUCCESS) {
                        return Single.just(true);
                    } else {
                        return Single.just(false);
                    }
                });
    }

    public Single<List<AccountModel>> getAccountList() {
        return mAccountManager.getAccountList();
    }

    public void changeLoginAccount(@NonNull AccountModel accountModel, @NonNull String password) {
        byte[] aesKey = WalletAppManager.getEncKey(password);
        mAccountManager.changeLoginAccount(accountModel, aesKey);
    }

    private Single<String> generateDefaultAccountName(String prefix) {
        return mAccountManager.getAccountCount()
                .flatMap(cnt -> {
                    return Single.just(prefix + (++cnt));
                });
    }

    public Single<GrpcAPI.WitnessList> getWitnessList() {
        return mTronManager.listWitnesses();
    }

    public Single<GrpcAPI.NodeList> getNodeList() {
        return mTronManager.listNodes();
    }

    public Single<Boolean> participateTokens(@Nullable String password, String tokenName, String issuerAddress, long amount) {
        return Single.fromCallable(() -> {
            byte[] toAddressBytes = AccountManager.decodeFromBase58Check(issuerAddress);

            Contract.ParticipateAssetIssueContract participateAssetIssueContract = mAccountManager
                    .participateAssetIssueContract(WalletAppManager.getEncKey(password), toAddressBytes, tokenName.getBytes(), amount);

            return mTronManager.createParticipateAssetIssueTransaction(participateAssetIssueContract);
        })
        .flatMap(transactionSingle -> transactionSingle)
        .flatMap(transaction -> {
            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> voteWitness(@Nullable String password, Map<String, String> witness) {
        return Single.fromCallable(() -> {
            Contract.VoteWitnessContract voteWitnessContract = mAccountManager.createVoteWitnessContract(WalletAppManager.getEncKey(password), witness);

            return mTronManager.createTransaction(voteWitnessContract);
        })
        .flatMap(transactionSingle -> transactionSingle)
        .flatMap(transaction -> {
            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> freezeBalance(@Nullable String password, long freezeBalance, long freezeDuration) {
        return Single.fromCallable(() -> {
            Contract.FreezeBalanceContract freezeBalanceContract = mAccountManager.createFreezeBalanceContract(WalletAppManager.getEncKey(password), freezeBalance, freezeDuration);

            return mTronManager.createTransaction(freezeBalanceContract);
        })
        .flatMap(transactionSingle -> transactionSingle)
        .flatMap(transaction -> {
            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public Single<Boolean> unfreezeBalance(@Nullable String password) {
        return Single.fromCallable(() -> {
            Contract.UnfreezeBalanceContract unfreezeBalanceContract = mAccountManager.createUnfreezeBalanceContract(WalletAppManager.getEncKey(password));

            return mTronManager.createTransaction(unfreezeBalanceContract);
        })
        .flatMap(transactionSingle -> transactionSingle)
        .flatMap(transaction -> {
            if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                throw new RuntimeException();
            }

            // sign transaction
            transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey(password), transaction);
            return mTronManager.broadcastTransaction(transaction);
        });
    }

    public boolean changePassword(String newPassword) {
        return false;
    }
}
