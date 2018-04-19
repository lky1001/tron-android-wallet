package com.devband.tronwalletforandroid.tron;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.spongycastle.util.encoders.Hex;
import org.tron.common.crypto.ECKey;
import org.tron.common.crypto.Hash;
import org.tron.common.crypto.SymmEncoder;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.FileUtil;
import org.tron.common.utils.Utils;

import java.io.File;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * https://github.com/tronprotocol/wallet-cli/blob/master/src/main/java/org/tron/walletserver/WalletClient.java
 */
public class WalletManager {

    private static final String LOG_TAG = WalletManager.class.getSimpleName();

    private static final String WALLET_FILE_PATH = "tron1/tron.dat";

    private static final int KEY_SIZE = 16;

    private ECKey mEcKey = null;
    private boolean mLoginState = false;

    public WalletManager() {

    }

    /**
     * Creates a new WalletClient with a random ECKey or no ECKey.
     */
    public WalletManager(boolean genEcKey) {
        if (genEcKey) {
            this.mEcKey = new ECKey(Utils.getRandom());
        }
    }


    public int store(@NonNull  String password) {
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

    public boolean login(String password) {
        this.mLoginState = checkPassWord(password);
        return mLoginState;
    }

    public int loadWalletByStorage(String password) {
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

        return ByteArray.toHexString(mEcKey.getAddress());
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
        char[] buf = new char[0x100];
        int len = FileUtil.readData(getWalletStorage(), buf);
        if (len != 226) {
            return null;
        }
        return String.valueOf(buf, 0, 32);
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
        char[] buf = new char[0x100];
        int len = FileUtil.readData(getWalletStorage(), buf);
        if (len != 226) {
            return null;
        }
        return String.valueOf(buf, 162, 64);
    }
}
