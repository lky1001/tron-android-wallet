package com.devband.tronwalletforandroid.tron;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.security.keystore.KeyStore;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.repository.AccountRepository;

import org.spongycastle.util.encoders.Hex;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Hash;
import org.tron.common.crypto.SymmEncoder;
import org.tron.common.utils.Base58;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.FileUtil;
import org.tron.common.utils.TransactionUtils;
import org.tron.core.config.Parameter;
import org.tron.protos.Protocol;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;

/**
 * https://github.com/tronprotocol/wallet-cli/blob/master/src/main/java/org/tron/walletserver/WalletClient.java
 */
public class AccountManager {

    private static final String LOG_TAG = AccountManager.class.getSimpleName();

    public static final long DEFAULT_ACCOUNT_INDEX = 1;

    private static final String ACCOUNT_FILE_PATH = "tron/tron.dat";

    private static final int KEY_SIZE = 16;

    private KeyStore mKeyStore;

    private AccountRepository mAccountRepository;

    private AccountModel mLoginAccountModel;

    public AccountManager(AccountRepository accountRepository, KeyStore keyStore) {
        this.mAccountRepository = accountRepository;
        this.mKeyStore = keyStore;
    }

    public Single<Integer> createAccount(@NonNull String nickname, @NonNull byte[] aesKey) {
        return createAddress(nickname, aesKey, false, null);
    }

    private Single<Integer> createAddress(@NonNull String accountName, @NonNull byte[] aesKey, boolean imported, ECKey temKey) {
        return Single.fromCallable(() -> {
            ECKey ecKey;

            if (imported) {
                ecKey = temKey;
            } else {
                ecKey= new ECKey(new SecureRandom());
            }

            byte[] privKeyPlain = ecKey.getPrivKeyBytes();
            byte[] privKeyEnced = SymmEncoder.AES128EcbEnc(privKeyPlain, aesKey);

            String privKeyStr = ByteArray.toHexString(privKeyEnced);

            String encodedKey = mKeyStore.encryptString(privKeyStr, Constants.ALIAS_ACCOUNT_KEY);

            if (imported) {
                AccountModel accountModel = mAccountRepository.loadByAccountKey(encodedKey);

                if (accountModel != null) {
                    return Tron.ERROR_EXIST_ACCOUNT;
                }
            }

            String address = encode58Check(ecKey.getAddress());

            mLoginAccountModel = AccountModel.builder()
                    .name(accountName)
                    .address(mKeyStore.encryptString(address, Constants.ALIAS_ADDRESS_KEY))
                    .account(encodedKey)
                    .imported(imported)
                    .build();

            long id = mAccountRepository.insertAccount(mLoginAccountModel).blockingGet();
            mLoginAccountModel.setId(id);

            return Tron.SUCCESS;
        });
    }

    public int loadAccountByRepository(@Nullable AccountModel accountModel, long lastSelectedIndex) {
        if (accountModel == null) {
            // todo - improve
            long idx = lastSelectedIndex > 0 ? lastSelectedIndex : DEFAULT_ACCOUNT_INDEX;
            accountModel = mAccountRepository.loadAccount(idx).blockingGet();
            if (accountModel == null) {
                accountModel = mAccountRepository.loadAccount();
            }
        }

        String priKeyEnced = getDecodedAccountKey(accountModel);

        if (TextUtils.isEmpty(priKeyEnced)) {
            return Tron.ERROR_ACCOUNT_DOES_NOT_EXIST;
        }

        this.mLoginAccountModel = accountModel;

        return Tron.SUCCESS;
    }

    public void logout() {
        this.mLoginAccountModel = null;
    }

    public String getLoginAddress() {
        return mKeyStore.decryptString(mLoginAccountModel.getAddress(), Constants.ALIAS_ADDRESS_KEY);
    }

    @Nullable
    public String getLoginPrivateKey(@NonNull byte[] aesKey) {
        String priKeyEnced = getLoginEncodedPriKey();

        ECKey ecKey = getEcKeyFromEncodedPrivateKey(priKeyEnced, aesKey);

        if (ecKey == null) {
            throw new IllegalArgumentException();
        }

        return ByteArray.toHexString(ecKey.getPrivKeyBytes());
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

    public Protocol.Transaction signTransaction(@NonNull byte[] aesKey, Protocol.Transaction transaction) {
        ECKey ecKey = getEcKeyFromEncodedPrivateKey(getLoginEncodedPriKey(), aesKey);

        transaction = TransactionUtils.setTimestamp(transaction);
        return TransactionUtils.sign(transaction, ecKey);
    }

    public Integer getAccountCount() {
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

    public Single<List<AccountModel>> getAccountList() {
        return mAccountRepository.loadAllAccounts();
    }

    public void changeLoginAccount(AccountModel accountModel) {
        if (mLoginAccountModel != null && mLoginAccountModel.getAccount().equals(accountModel.getAccount())) {
            return;
        }

        loadAccountByRepository(accountModel, accountModel.getId());
    }

    public Single<Integer> importAccount(@NonNull String nickname, @NonNull String privateKey, @NonNull byte[] aesKey, boolean imported) {
        ECKey temKey;
        try {
            temKey = ECKey.fromPrivate(ByteArray.fromHexString(privateKey));
            return createAddress(nickname, aesKey, imported, temKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Single.fromCallable(() -> Tron.ERROR_PRIVATE_KEY);
        }
    }

    public static boolean priKeyValid(byte[] priKey) {
        if (priKey.length != 32) {
            return false;
        }

        return true;
    }

    @Nullable
    private ECKey getEcKeyFromEncodedPrivateKey(@NonNull String priKeyEnced, @NonNull byte[] aesKey) {
        //dec priKey
        byte[] priKeyAscEnced = priKeyEnced.getBytes();
        byte[] priKeyHexEnced = Hex.decode(priKeyAscEnced);
        byte[] priKeyHexPlain = SymmEncoder.AES128EcbDec(priKeyHexEnced, aesKey);
        String priKeyPlain = Hex.toHexString(priKeyHexPlain);

        try {
            BigInteger priK = new BigInteger(priKeyPlain, KEY_SIZE);
            return ECKey.fromPrivate(priK);
        } catch (Exception e) {
            return null;
        }
    }

    private String getLoginEncodedPriKey() {
        return getDecodedAccountKey(mLoginAccountModel);
    }

    private String getDecodedAccountKey(@NonNull AccountModel accountModel) {
        return mKeyStore.decryptString(accountModel.getAccount(), Constants.ALIAS_ACCOUNT_KEY);
    }

    // todo - remove when all user updated above 1.2.5
    public void migrationAccount(@NonNull String password) {
        List<AccountModel> accountList = mAccountRepository.loadAllAccounts().blockingGet();
        for (AccountModel accountModel : accountList) {
            String priKeyEnced = accountModel.getAccount().substring(162, 226);
            String encPriKey = mKeyStore.encryptString(priKeyEnced, Constants.ALIAS_ACCOUNT_KEY);

            String address = encode58Check(getEcKeyFromEncodedPrivateKey(priKeyEnced, WalletAppManager.getEncKey(password)).getAddress());
            String encAddress = mKeyStore.encryptString(address, Constants.ALIAS_ADDRESS_KEY);

            accountModel.setAddress(encAddress);
            accountModel.setAccount(encPriKey);

            mAccountRepository.updateAccount(accountModel).blockingGet();
        }
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        List<AccountModel> accountList = mAccountRepository.loadAllAccounts().blockingGet();
        
        for (AccountModel accountModel : accountList) {
            String priKeyEnced = mKeyStore.decryptString(accountModel.getAccount(), Constants.ALIAS_ACCOUNT_KEY);

            byte[] oldAesKey = WalletAppManager.getEncKey(oldPassword);

            if (priKeyEnced == null || oldAesKey == null) {
                return false;
            }

            byte[] privKeyPlain = getEcKeyFromEncodedPrivateKey(priKeyEnced, oldAesKey).getPrivKeyBytes();

            byte[] newAesKey = WalletAppManager.getEncKey(newPassword);

            if (newAesKey == null) {
                return false;
            }

            byte[] privKeyEnced = SymmEncoder.AES128EcbEnc(privKeyPlain, newAesKey);

            String privKeyStr = ByteArray.toHexString(privKeyEnced);
            String encPrivKey = mKeyStore.encryptString(privKeyStr, Constants.ALIAS_ACCOUNT_KEY);

            accountModel.setAccount(encPrivKey);
        }

        for (AccountModel accountModel : accountList) {
            accountModel.setUpdated(Calendar.getInstance().getTime());
            mAccountRepository.updateAccount(accountModel).blockingGet();
        }

        return true;
    }

    public void removeAccount(long accountId, String accountName) {
        AccountModel accountModel = mAccountRepository.loadAccount(accountId).blockingGet();

        if (accountModel != null && accountName.equals(accountModel.getName())) {
            mAccountRepository.delete(accountModel);
        }

        mLoginAccountModel = mAccountRepository.loadAccount();
    }

    public String decryptAddress(String address) {
        return mKeyStore.decryptString(address, Constants.ALIAS_ADDRESS_KEY);
    }
}