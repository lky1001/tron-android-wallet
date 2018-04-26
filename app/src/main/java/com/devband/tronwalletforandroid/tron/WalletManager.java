package com.devband.tronwalletforandroid.tron;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.devband.tronwalletforandroid.database.model.WalletModel;
import com.devband.tronwalletforandroid.tron.repository.FileRepository;
import com.devband.tronwalletforandroid.tron.repository.LocalDbRepository;
import com.devband.tronwalletforandroid.tron.repository.WalletRepository;
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
import java.util.Arrays;

/**
 * https://github.com/tronprotocol/wallet-cli/blob/master/src/main/java/org/tron/walletserver/WalletClient.java
 */
public class WalletManager {

    private static final String LOG_TAG = WalletManager.class.getSimpleName();

    public static final int WALLET_LOCAL_DB = 1;
    public static final int WALLET_FILE = 2;

    private static final String WALLET_FILE_PATH = "tron/tron.dat";

    private static final int KEY_SIZE = 16;

    private ECKey mEcKey = null;
    private boolean mLoginState = false;

    private WalletRepository mWalletRepository;

    private WalletModel mLoginWalletModel;

    public WalletManager() {

    }

    public WalletManager(int walletType, Context context) {
        this(true, walletType, context);
    }

    public WalletManager(boolean genEcKey, Context context) {
        this(genEcKey, WALLET_LOCAL_DB, context);
    }
    /**
     * Creates a new WalletClient with a random ECKey or no ECKey.
     */
    public WalletManager(boolean genEcKey, int walletType, Context context) {
        if (genEcKey) {
            this.mEcKey = new ECKey(Utils.getRandom());
        }

        if (walletType == WALLET_LOCAL_DB) {
            mWalletRepository = new LocalDbRepository(context);
        } else if (walletType == WALLET_FILE) {
            mWalletRepository = new FileRepository();
        }
    }

    public WalletManager(String privateKey, Context context) {
        this(privateKey, WALLET_LOCAL_DB, context);
    }

    public WalletManager(String privateKey, int walletType, Context context) {
        ECKey temKey = null;
        try {
            BigInteger priK = new BigInteger(privateKey, KEY_SIZE);
            temKey = ECKey.fromPrivate(priK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.mEcKey = temKey;

        if (walletType == WALLET_LOCAL_DB) {
            mWalletRepository = new LocalDbRepository(context);
        } else if (walletType == WALLET_FILE) {
            mWalletRepository = new FileRepository();
        }
    }

    public int genWallet(@NonNull String walletName, String password) {
        if (this.mEcKey == null || this.mEcKey.getPrivKey() == null) {
            return Tron.ERROR_PRIVATE_KEY;
        }

        byte[] pwd = getPassWord(password);
        String pwdAsc = ByteArray.toHexString(pwd);
        byte[] privKeyPlain = this.mEcKey.getPrivKeyBytes();
        //encrypted by password
        byte[] aseKey = getEncKey(password);
        byte[] privKeyEnced = SymmEncoder.AES128EcbEnc(privKeyPlain, aseKey);
        String privKeyStr = ByteArray.toHexString(privKeyEnced);
        byte[] pubKeyBytes = this.mEcKey.getPubKey();
        String pubKeyStr = ByteArray.toHexString(pubKeyBytes);

        mLoginWalletModel = WalletModel.builder()
                .name(walletName)
                .wallet(pwdAsc + pubKeyStr + privKeyStr)
                .build();

        return Tron.SUCCESS;
    }

    public int storeWallet() {
        if (mLoginWalletModel == null) {
            return Tron.ERROR;
        }

        // todo - check duplication wallet info
        boolean result = mWalletRepository.storeWallet(mLoginWalletModel);

        if (result) {
            return Tron.SUCCESS;
        } else {
            return Tron.ERROR;
        }
    }

    public int store(@NonNull String password) {
        if (this.mEcKey == null || this.mEcKey.getPrivKey() == null) {
            return Tron.ERROR_PRIVATE_KEY;
        }

        byte[] pwd = getPassWord(password);
        String pwdAsc = ByteArray.toHexString(pwd);
        byte[] privKeyPlain = this.mEcKey.getPrivKeyBytes();
        //System.out.println("privKey:" + ByteArray.toHexString(privKeyPlain));
        //encrypted by password
        byte[] aseKey = getEncKey(password);
        byte[] privKeyEnced = SymmEncoder.AES128EcbEnc(privKeyPlain, aseKey);
        String privKeyStr = ByteArray.toHexString(privKeyEnced);
        byte[] pubKeyBytes = this.mEcKey.getPubKey();
        String pubKeyStr = ByteArray.toHexString(pubKeyBytes);

        // todo - file save to external storage
        File file = getWalletStorage();

        if (file == null) {
            return Tron.ERROR_ACCESS_STORAGE;
        }
        // SAVE PASSWORD
        FileUtil.saveData(file, pwdAsc, false);//ofset:0 len:32
        // SAVE PUBKEY
        FileUtil.saveData(file, pubKeyStr, true);//ofset:32 len:130
        // SAVE PRIKEY
        FileUtil.saveData(file, privKeyStr, true);

        return Tron.SUCCESS;
    }

    public boolean backUpWalletToStorage(String password) {
        return true;
    }

    public boolean login(String password) {
        this.mLoginState = checkPassWord(password);
        return mLoginState;
    }

    public int loadWalletByRepository(String password) {
        String priKeyEnced = loadPriKey();

        if (priKeyEnced == null) {
            return Tron.ERROR_WALLET_DOES_NOT_EXIST;
        }

        //dec priKey
        byte[] priKeyAscEnced = priKeyEnced.getBytes();
        byte[] priKeyHexEnced = Hex.decode(priKeyAscEnced);
        byte[] aesKey = getEncKey(password);
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

        return Tron.SUCCESS;
    }

    public boolean isLoginState() {
        return this.mLoginState;
    }

    public void logout() {
        this.mLoginState = false;
    }

    @Nullable
    public String getAddress() {
        if (!mLoginState) {
            return null;
        }

        if (mEcKey == null) {
            return getAddressByStorage();
        }

        return encode58Check(mEcKey.getAddress());
//        return ByteArray.toHexString(mEcKey.getAddress());
    }

    @Nullable
    public String getPrivateKey() {
        if (!mLoginState) {
            return null;
        }

        if (mEcKey == null) {
            return loadPriKey();
        }

        return ByteArray.toHexString(mEcKey.getPrivKeyBytes());
    }

    public boolean checkPassWord(String password) {
        byte[] pwd = getPassWord(password);
        if (pwd == null) {
            return false;
        }
        String pwdAsc = ByteArray.toHexString(pwd);
        String pwdInstore = loadPassword();
        return pwdAsc.equals(pwdInstore);
    }

    @Nullable
    private String loadPassword() {
        // todo - load from db
        return mLoginWalletModel.getWallet().substring(0, 32);
    }

    @Nullable
    private byte[] getPassWord(String password) {
        if (!passwordValid(password)) {
            return null;
        }
        byte[] pwd;
        pwd = Hash.sha256(password.getBytes());
        pwd = Hash.sha256(pwd);
        pwd = Arrays.copyOfRange(pwd, 0, KEY_SIZE);
        return pwd;
    }

    @Nullable
    private byte[] getEncKey(String password) {
        if (!passwordValid(password)) {
            return null;
        }
        byte[] encKey;
        encKey = Hash.sha256(password.getBytes());
        encKey = Arrays.copyOfRange(encKey, 0, KEY_SIZE);
        return encKey;
    }

    private File getWalletStorage() {
        File file = new File(Environment.getExternalStorageDirectory().getPath()
                + File.separator + WALLET_FILE_PATH);

        if (!file.getParentFile().isDirectory()) {
            if (!file.getParentFile().mkdirs()) {
                Log.e(LOG_TAG, "Directory not created");
                return null;
            }
        }

        return file;
    }

    public static boolean passwordValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        if (password.length() < Tron.MIN_PASSWORD_LENGTH) {
            return false;
        }
        return true;
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
        int len = FileUtil.readData(getWalletStorage(), buf);
        if (len != 226) {
            return null;
        }
        return String.valueOf(buf, 32, 130);
    }

    private String loadPriKey() {
        if (mLoginWalletModel == null) {
            mLoginWalletModel = mWalletRepository.loadWallet(1);
        }
        char[] buf = new char[0x100];
        int len = FileUtil.readData(getWalletStorage(), buf);
        if (len != 226) {
            return null;
        }
        return String.valueOf(buf, 162, 64);
    }

    public static byte[] decodeFromBase58Check(String addressBase58) {
        if (addressBase58 == null || addressBase58.length() == 0) {
            return null;
        }
        if (addressBase58.length() != Parameter.CommonConstant.BASE58CHECK_ADDRESS_SIZE) {
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
        if (preFixbyte != Parameter.CommonConstant.ADD_PRE_FIX_BYTE) {
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

        Contract.TransferContract contract = createTransferContract(toAddress, ownerAddress, amount);

        return contract;
    }

    private static Contract.TransferContract createTransferContract(byte[] to, byte[] owner,
            long amount) {
        Contract.TransferContract.Builder builder = Contract.TransferContract.newBuilder();
        ByteString bsTo = ByteString.copyFrom(to);
        ByteString bsOwner = ByteString.copyFrom(owner);
        builder.setToAddress(bsTo);
        builder.setOwnerAddress(bsOwner);
        builder.setAmount(amount);

        return builder.build();
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

    public int getWalletCount() {
        return mWalletRepository.countWallets();
    }
}

