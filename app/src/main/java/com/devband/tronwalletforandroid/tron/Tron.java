package com.devband.tronwalletforandroid.tron;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.devband.tronlib.TronNetwork;
import com.devband.tronlib.tronscan.Account;
import com.devband.tronlib.tronscan.Balance;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.common.Hex2Decimal;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.database.model.Trc10AssetModel;
import com.devband.tronwalletforandroid.tron.exception.InvalidAddressException;
import com.devband.tronwalletforandroid.tron.exception.InvalidPasswordException;
import com.google.protobuf.ByteString;

import org.spongycastle.util.encoders.Hex;
import org.tron.api.GrpcAPI;
import org.tron.common.utils.AbiUtil;
import org.tron.common.utils.ByteArray;
import org.tron.core.exception.EncodingException;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Single;
import timber.log.Timber;

public class Tron {

    public static final int SUCCESS = 1;
    public static final int ERROR_INVALID_PASSWORD = -1;
    public static final int ERROR_PRIVATE_KEY = -2;
    public static final int ERROR_ACCESS_STORAGE = -3;
    public static final int ERROR_ACCOUNT_DOES_NOT_EXIST = -4;
    public static final int ERROR_NEED_LOGIN = -5;
    public static final int ERROR_LOGIN = -6;
    public static final int ERROR_EXIST_ACCOUNT = -7;
    public static final int ERROR_INVALID_ADDRESS = -8;
    public static final int ERROR_INVALID_TRC20_CONTRACT = -9;
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

    private TokenManager mTokenManager;

    private boolean mFailConnectNode;

    public Tron(Context context, TronNetwork tronNetwork, CustomPreference customPreference, AccountManager accountManager,
            WalletAppManager walletAppManager, TokenManager tokenManager) {
        this.mContext = context;
        this.mTronNetwork = tronNetwork;
        this.mCustomPreference = customPreference;
        this.mAccountManager = accountManager;
        this.mWalletAppManager = walletAppManager;
        this.mTokenManager = tokenManager;
        init();

        this.mTokenManager.setTron(this);
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
            if (result == SUCCESS) {
                String accountNickname = generateDefaultAccountName(nickname);

                byte[] aesKey = WalletAppManager.getEncKey(password);
                return mAccountManager.createAccount(accountNickname, aesKey);
            } else {
                return Single.just(ERROR_INVALID_PASSWORD);
            }
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
        mAccountManager.loadAccountByRepository(null, mCustomPreference.getLastSelectedAccountId());
    }

    public int login(@NonNull String password) {
        int result = mWalletAppManager.login(password);

        if (result != WalletAppManager.SUCCESS) {
            return ERROR_INVALID_PASSWORD;
        }

        mAccountManager.loadAccountByRepository(null, mCustomPreference.getLastSelectedAccountId());

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

    public Single<Account> getEncryptAccount(@NonNull String address) {
        return getAccount(mAccountManager.decryptAddress(address));
    }

    public Single<Account> getAccount(@NonNull String address) {
        return mTronNetwork.getAccountInfo(address)
                .map(accountInfo -> {
                    for (Balance trc10TokenBalance : accountInfo.getTrc10TokenBalances()) {
                        trc10TokenBalance.setDisplayName(mTokenManager.getTokenInfo(trc10TokenBalance.getName()).blockingGet().getName());
                    }

                    return accountInfo;
                });
    }

    public Trc10AssetModel getTrc10Asset(String tokenId) {
        return mTokenManager.getTokenInfo(tokenId).blockingGet();
    }

    public Single<Protocol.Account> queryAccount(@NonNull String address) {
        return Single.fromCallable(() -> {
            if (!TextUtils.isEmpty(address)) {
                byte[] addressBytes = AccountManager.decodeFromBase58Check(address);

                if (addressBytes == null) {
                    throw new IllegalArgumentException("Invalid address.");
                }

                return addressBytes;
            } else {
                throw new IllegalArgumentException("address is required.");
            }
        }).flatMap(addressBytes -> mTronManager.queryAccount(addressBytes));
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
        mCustomPreference.setLastSelectedAccountId(accountModel.getId());
        return true;
    }

    private String generateDefaultAccountName(String prefix) {
        return prefix + (mAccountManager.getAccountCount() + 1);
    }

    public int getAccountCount() {
        return mAccountManager.getAccountCount();
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

    public boolean changePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        if (mWalletAppManager.changePassword(oldPassword, newPassword)) {
            if (!mAccountManager.changePassword(oldPassword, newPassword)) {
                // rollback
                mWalletAppManager.changePassword(newPassword, oldPassword);
                return false;
            }
            return true;
        }

        return false;
    }

    public Single<Integer> createWallet(String password) {
        return Single.fromCallable(() -> mWalletAppManager.createWallet(password));
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

    public void removeAccount(long accountId, String accountName) {
        mAccountManager.removeAccount(accountId, accountName);
        mCustomPreference.setLastSelectedAccountId(mAccountManager.getLoginAccount().getId());
    }

    public Single<Protocol.SmartContract> getSmartContract(@NonNull String address) {
        return Single.fromCallable(() -> AccountManager.decodeFromBase58Check(address))
                .flatMap(addressBytes -> mTronManager.getSmartContract(addressBytes));
    }

    public Single<GrpcAPI.TransactionExtention> getTrc20Balance(@NonNull String ownerAddress, String contractAddress) {
        return Single.fromCallable(() -> AccountManager.decodeFromBase58Check(ownerAddress))
                .flatMap(addressBytes -> {
                    String transferMethod = "balanceOf(address)";
                    String transferParams = "\""+ ownerAddress + "\"";

                    String contractTrigger = "";

                    try {
                        contractTrigger = AbiUtil.parseMethod(transferMethod, transferParams);
                    } catch (EncodingException e ) {
                        e.printStackTrace();
                    }

                    byte[] input = Hex.decode(contractTrigger);
                    return mTronManager.triggerContract(addressBytes, AccountManager.decodeFromBase58Check(contractAddress), 0L, input, 1_000_000_000L, 0, null);
                });
    }

    /**
     * call start contract
     * @param contractAddress
     * @param callValue transfer trx value
     * @param input
     * @param feeLimit
     * @param tokenCallValue transfer token value
     * @param tokenId transfer token id, optional
     * @return
     */
    public Single<Boolean> callQueryContract(@NonNull String ownerAddress, byte[] contractAddress, long callValue,
            byte[] input, long feeLimit, long tokenCallValue, @Nullable String tokenId) {
        return Single.fromCallable(() -> AccountManager.decodeFromBase58Check(ownerAddress))
                .flatMap(addressBytes -> mTronManager.triggerContract(addressBytes, contractAddress, callValue, input, feeLimit, tokenCallValue, tokenId))
                .map(transactionExtention -> {
                    if (transactionExtention == null || !transactionExtention.getResult().getResult()) {
                        Timber.d("RPC create call trx failed!");
                        Timber.d("Code = " + transactionExtention != null ? String.valueOf(transactionExtention.getResult().getCode()) : "null");
                        Timber.d("Message = " + transactionExtention != null ? transactionExtention.getResult().getMessage().toStringUtf8() : "null");
                        return false;
                    }

                    Protocol.Transaction resultTransaction = transactionExtention.getTransaction();

                    if (resultTransaction.getRetCount() != 0 &&
                            transactionExtention.getConstantResult(0) != null &&
                            transactionExtention.getResult() != null) {
                        byte[] result = transactionExtention.getConstantResult(0).toByteArray();
                        System.out.println("message:" + resultTransaction.getRet(0).getRet());
                        System.out.println(":" + ByteArray
                                .toStr(transactionExtention.getResult().getMessage().toByteArray()));
                        System.out.println("Result:" + Hex2Decimal.hex2Decimal(Hex.toHexString(result)));
                        return true;
                    }

                    GrpcAPI.TransactionExtention.Builder texBuilder = GrpcAPI.TransactionExtention.newBuilder();
                    Protocol.Transaction.Builder transBuilder = Protocol.Transaction.newBuilder();
                    Protocol.Transaction.raw.Builder rawBuilder = transactionExtention.getTransaction().getRawData()
                            .toBuilder();
                    rawBuilder.setFeeLimit(feeLimit);
                    transBuilder.setRawData(rawBuilder);
                    for (int i = 0; i < transactionExtention.getTransaction().getSignatureCount(); i++) {
                        ByteString s = transactionExtention.getTransaction().getSignature(i);
                        transBuilder.setSignature(i, s);
                    }
                    for (int i = 0; i < transactionExtention.getTransaction().getRetCount(); i++) {
                        Protocol.Transaction.Result r = transactionExtention.getTransaction().getRet(i);
                        transBuilder.setRet(i, r);
                    }
                    texBuilder.setTransaction(transBuilder);
                    texBuilder.setResult(transactionExtention.getResult());
                    texBuilder.setTxid(transactionExtention.getTxid());
                    transactionExtention = texBuilder.build();

                    GrpcAPI.Return ret = transactionExtention.getResult();

                    if (!ret.getResult()) {
                        System.out.println("Code = " + ret.getCode());
                        System.out.println("Message = " + ret.getMessage().toStringUtf8());
                        return false;
                    }

                    Protocol.Transaction transaction = transactionExtention.getTransaction();
                    if (transaction == null || transaction.getRawData().getContractCount() == 0) {
                        System.out.println("Transaction is empty");
                        return false;
                    }
                    System.out.println(
                            "Receive txid = " + ByteArray.toHexString(transactionExtention.getTxid().toByteArray()));
                    transaction = mAccountManager.signTransaction(WalletAppManager.getEncKey("12345678"), transaction);
                    return mTronManager.broadcastTransaction(transaction).blockingGet();
                });
    }

    public Single<Integer> checkTrc20Contract(String contractAddress) {
        return Single.fromCallable(() -> {
            byte[] addressByte = AccountManager.decodeFromBase58Check(contractAddress);

            if (addressByte == null) {
                return ERROR_INVALID_ADDRESS;
            }

            Protocol.SmartContract smartContract = getSmartContract(contractAddress).blockingGet();

            if (smartContract == null || smartContract.getAbi() == null) {
                return ERROR_INVALID_TRC20_CONTRACT;
            }

            List<org.tron.protos.Protocol.SmartContract.ABI.Entry> entryList = smartContract.getAbi().getEntrysList();

            boolean hasBalanceFunction = false;

            for (Protocol.SmartContract.ABI.Entry entry : entryList) {
                if ("balanceOf".equalsIgnoreCase(entry.getName())) {
                    hasBalanceFunction = true;
                    break;
                }
            }

            if (!hasBalanceFunction) {
                return ERROR_INVALID_TRC20_CONTRACT;
            }

            return SUCCESS;
        });
    }

    public Single<Contract.AssetIssueContract> getAssetIssueById(String id) {
        return mTronManager.getAssetIssueById(id);
    }

    public boolean checkPassword(String password) {
        return mWalletAppManager.checkPassword(password);
    }
}
