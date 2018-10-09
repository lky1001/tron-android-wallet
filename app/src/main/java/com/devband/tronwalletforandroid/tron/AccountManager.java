package com.devband.tronwalletforandroid.tron;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.devband.tronwalletforandroid.database.AppDatabase;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.repository.AccountRepository;
import com.devband.tronwalletforandroid.tron.repository.FileRepository;
import com.devband.tronwalletforandroid.tron.repository.LocalDbRepository;
import com.google.protobuf.ByteString;

import org.spongycastle.util.encoders.Hex;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Hash;
import org.tron.common.crypto.SymmEncoder;
import org.tron.common.utils.Base58;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.FileUtil;
import org.tron.common.utils.TransactionUtils;
import org.tron.common.utils.Utils;
import org.tron.core.config.Parameter;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;

/**
 * https://github.com/tronprotocol/wallet-cli/blob/master/src/main/java/org/tron/walletserver/WalletClient.java
 */
public class AccountManager {

    private static final String LOG_TAG = AccountManager.class.getSimpleName();

    public static final int PERSISTENT_LOCAL_DB = 1;
    public static final int PERSISTENT_FILE = 2;

    public static final int DEFAULT_ACCOUNT_INDEX = 1;

    private static final String ACCOUNT_FILE_PATH = "tron/tron.dat";

    private static final int KEY_SIZE = 16;

    private ECKey mEcKey = null;

    private AccountRepository mAccountRepository;

    private AccountModel mLoginAccountModel;

    public AccountManager(boolean genEcKey, int persistentType, AppDatabase appDatabase) {
        if (genEcKey) {
            this.mEcKey = new ECKey(Utils.getRandom());
        }

        initAccountRepository(persistentType, appDatabase);
    }

    private void initAccountRepository(int persistentType, AppDatabase appDatabase) {
        if (persistentType == PERSISTENT_LOCAL_DB) {
            mAccountRepository = new LocalDbRepository(appDatabase);
        } else if (persistentType == PERSISTENT_FILE) {
            mAccountRepository = new FileRepository();
        }
    }

    public Single<Integer> genAccount(@NonNull String accountName, @NonNull byte[] aesKey) {
        if (this.mEcKey == null || this.mEcKey.getPrivKey() == null) {
            return Single.fromCallable(() -> Tron.ERROR_PRIVATE_KEY);
        }

        return createAddress(accountName, aesKey, false);
    }

    private Single<Integer> createAddress(@NonNull String accountName, @NonNull byte[] aesKey, boolean imported) {
        return Single.fromCallable(() -> {
            byte[] privKeyPlain = this.mEcKey.getPrivKeyBytes();
            byte[] privKeyEnced = SymmEncoder.AES128EcbEnc(privKeyPlain, aesKey);

             String privKeyStr = ByteArray.toHexString(privKeyEnced);
            byte[] pubKeyBytes = this.mEcKey.getPubKey();
            String pubKeyStr = ByteArray.toHexString(pubKeyBytes);

            String accountKey = pubKeyStr + privKeyStr;

            if (imported) {
                AccountModel accountModel = mAccountRepository.loadByAccountKey(accountKey).blockingGet();

                if (accountModel != null) {
                    return Tron.ERROR_EXIST_ACCOUNT;
                }
            }

            mLoginAccountModel = AccountModel.builder()
                    .name(accountName)
                    .account(accountKey)
                    .imported(imported)
                    .build();

            mAccountRepository.storeAccount(mLoginAccountModel).blockingGet();

            return Tron.SUCCESS;
        });
    }

    public int loadAccountByRepository(@Nullable AccountModel accountModel, @NonNull byte[] aesKey) {
        if (accountModel == null) {
            // todo - improve
            accountModel = mAccountRepository.loadAccount(DEFAULT_ACCOUNT_INDEX).blockingGet();
        }

        String priKeyEnced = accountModel.getAccount().substring(130, 194);

        if (TextUtils.isEmpty(priKeyEnced)) {
            return Tron.ERROR_ACCOUNT_DOES_NOT_EXIST;
        }

        //dec priKey
        byte[] priKeyAscEnced = priKeyEnced.getBytes();
        byte[] priKeyHexEnced = Hex.decode(priKeyAscEnced);
        byte[] priKeyHexPlain = SymmEncoder.AES128EcbDec(priKeyHexEnced, aesKey);
        String priKeyPlain = Hex.toHexString(priKeyHexPlain);

        ECKey temKey = null;

        try {
            BigInteger priK = new BigInteger(priKeyPlain, KEY_SIZE);
            temKey = ECKey.fromPrivate(priK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Tron.ERROR_INVALID_PASSWORD;
        }

        this.mEcKey = temKey;
        this.mLoginAccountModel = accountModel;

        return Tron.SUCCESS;
    }

    public void logout() {
        this.mLoginAccountModel = null;
    }

    @Nullable
    public String getLoginAddress() {
        if (mEcKey == null) {
            return getAddressByStorage();
        }

        return encode58Check(mEcKey.getAddress());
//        return ByteArray.toHexString(mEcKey.getLoginAddress());
    }

    @Nullable
    public String getLoginPrivateKey() {
        if (mEcKey == null) {
            return loadPriKey();
        }

        return ByteArray.toHexString(mEcKey.getPrivKeyBytes());
    }

    private File getAccountStorage() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + ACCOUNT_FILE_PATH);

        if (!file.getParentFile().isDirectory()) {
            if (!file.getParentFile().mkdirs()) {
                Log.e(LOG_TAG, "Directory not created");
                return null;
            }
        }

        return file;
    }

    @Nullable
    private String getAddressByStorage() {
        try {
            String pubKey = loadPubKey(); //04 PubKey[128]
            if (pubKey == null || "".equals(pubKey)) {
                return null;
            }
            byte[] pubKeyAsc = pubKey.getBytes();
            byte[] pubKeyHex = Hex.decode(pubKeyAsc);
            ECKey eccKey = ECKey.fromPublicOnly(pubKeyHex);
            return ByteArray.toHexString(eccKey.getAddress());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String loadPubKey() {
        char[] buf = new char[0x100];
        int len = FileUtil.readData(getAccountStorage(), buf);
        if (len != 226) {
            return null;
        }
        return String.valueOf(buf, 32, 130);
    }

    private String loadPriKey() {
        return mLoginAccountModel.getAccount().substring(131, 195);
    }

    public static byte[] decodeFromBase58Check(String addressBase58) {
        if (addressBase58 == null || addressBase58.length() == 0) {
            return null;
        }

        byte[] address = decode58Check(addressBase58);
        if (!addressValid(address)) {
            return null;
        }
        return address;
    }

    private static byte[] decode58Check(String input) {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            return null;
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Hash.sha256(decodeData);
        byte[] hash1 = Hash.sha256(hash0);
        if (hash1[0] == decodeCheck[decodeData.length] &&
                hash1[1] == decodeCheck[decodeData.length + 1] &&
                hash1[2] == decodeCheck[decodeData.length + 2] &&
                hash1[3] == decodeCheck[decodeData.length + 3]) {
            return decodeData;
        }
        return null;
    }

    private static boolean addressValid(byte[] address) {
        if (address == null || address.length == 0) {
            return false;
        }
        if (address.length != Parameter.CommonConstant.ADDRESS_SIZE) {
            return false;
        }
        byte preFixbyte = address[0];
        if (preFixbyte != Parameter.CommonConstant.getAddressPrefix()) {
            return false;
        }
        //Other rule;
        return true;
    }

    public static String encode58Check(byte[] input) {
        byte[] hash0 = Hash.sha256(input);
        byte[] hash1 = Hash.sha256(hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
    }

    public Contract.TransferContract createTransferContract(@NonNull byte[] toAddress, long amount) {
        byte[] ownerAddress = mEcKey.getAddress();

        Contract.TransferContract.Builder builder = Contract.TransferContract.newBuilder();
        ByteString bsTo = ByteString.copyFrom(toAddress);
        ByteString bsOwner = ByteString.copyFrom(ownerAddress);
        builder.setToAddress(bsTo);
        builder.setOwnerAddress(bsOwner);
        builder.setAmount(amount);

        Contract.TransferContract contract = builder.build();;

        return contract;
    }

    public Contract.FreezeBalanceContract createFreezeBalanceContract(long frozenBalance, long frozenDuration) {
        byte[] ownerAddress = mEcKey.getAddress();

        Contract.FreezeBalanceContract.Builder builder = Contract.FreezeBalanceContract.newBuilder();
        ByteString byteAddreess = ByteString.copyFrom(ownerAddress);

        builder.setOwnerAddress(byteAddreess).setFrozenBalance(frozenBalance)
                .setFrozenDuration(frozenDuration);

        return builder.build();
    }

    public Contract.UnfreezeBalanceContract createUnfreezeBalanceContract() {
        byte[] ownerAddress = mEcKey.getAddress();

        Contract.UnfreezeBalanceContract.Builder builder = Contract.UnfreezeBalanceContract
                .newBuilder();
        ByteString byteAddreess = ByteString.copyFrom(ownerAddress);

        builder.setOwnerAddress(byteAddreess);

        return builder.build();
    }

    public Contract.TransferAssetContract createTransferAssetTransaction(@NonNull byte[] toAddress, @NonNull byte[] assetName, long amount) {
        byte[] ownerAddress = mEcKey.getAddress();

        Contract.TransferAssetContract.Builder builder = Contract.TransferAssetContract.newBuilder();
        ByteString bsTo = ByteString.copyFrom(toAddress);
        ByteString bsName = ByteString.copyFrom(assetName);
        ByteString bsOwner = ByteString.copyFrom(ownerAddress);
        builder.setToAddress(bsTo);
        builder.setAssetName(bsName);
        builder.setOwnerAddress(bsOwner);
        builder.setAmount(amount);

        Contract.TransferAssetContract contract = builder.build();

        return contract;
    }

    public Protocol.Transaction signTransaction(Protocol.Transaction transaction) {
        if (this.mEcKey == null || this.mEcKey.getPrivKey() == null) {
            return null;
        }
        transaction = TransactionUtils.setTimestamp(transaction);
        return TransactionUtils.sign(transaction, this.mEcKey);
    }

    public ECKey getEcKey() {
        return mEcKey;
    }

    public Single<Integer> getAccountCount() {
        return mAccountRepository.countAccount();
    }

    @Nullable
    public AccountModel getLoginAccount() {
        return mLoginAccountModel;
    }

    public Single<Boolean> changeLoginAccountName(@NonNull String accountName) {
        mLoginAccountModel.setName(accountName);

        return mAccountRepository.updateAccount(mLoginAccountModel);
    }

    public Single<Integer> createAccount(@NonNull String nickname, @NonNull byte[] aesKey) {
        this.mEcKey = new ECKey(Utils.getRandom());

        return createAddress(nickname, aesKey, false);
    }

    public Single<List<AccountModel>> getAccountList() {
        return mAccountRepository.loadAllAccounts();
    }

    public void changeLoginAccount(AccountModel accountModel, @NonNull byte[] aesKey) {
        if (mLoginAccountModel != null && mLoginAccountModel.getAccount().equals(accountModel.getAccount())) {
            return;
        }

        loadAccountByRepository(accountModel, aesKey);
    }

    public Single<Integer> importAccount(@NonNull String nickname, @NonNull String privateKey, @NonNull byte[] aesKey, boolean imported) {
        ECKey temKey = null;
        try {
            temKey = ECKey.fromPrivate(ByteArray.fromHexString(privateKey));
        } catch (Exception ex) {
            ex.printStackTrace();
            Single.fromCallable(() -> Tron.ERROR_PRIVATE_KEY);
        }
        this.mEcKey = temKey;

        return createAddress(nickname, aesKey, imported);
    }

    public static boolean priKeyValid(byte[] priKey) {
        if (priKey.length != 32) {
            return false;
        }

        return true;
    }

    public Contract.VoteWitnessContract createVoteWitnessContract(Map<String, String> witness) {
        byte[] ownerAddress = mEcKey.getAddress();

        Contract.VoteWitnessContract.Builder builder = Contract.VoteWitnessContract.newBuilder();
        builder.setOwnerAddress(ByteString.copyFrom(ownerAddress));
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
    }

    public Contract.ParticipateAssetIssueContract participateAssetIssueContract(byte[] to,
            byte[] assertName, long amount) {
        byte[] ownerAddress = mEcKey.getAddress();

        Contract.ParticipateAssetIssueContract.Builder builder = Contract.ParticipateAssetIssueContract
                .newBuilder();
        ByteString bsTo = ByteString.copyFrom(to);
        ByteString bsName = ByteString.copyFrom(assertName);
        ByteString bsOwner = ByteString.copyFrom(ownerAddress);
        builder.setToAddress(bsTo);
        builder.setAssetName(bsName);
        builder.setOwnerAddress(bsOwner);
        builder.setAmount(amount);

        return builder.build();
    }
}

