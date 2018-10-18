package com.devband.tronwalletforandroid.common.security.keystore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.common.CustomPreference;

import org.tron.common.utils.ByteArray;
import org.tron.common.utils.Utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

public class KeyStoreApi15Impl implements KeyStore {

    private static final int KEY_SIZE = 128;

    private final CustomPreference mCustomPreference;

    @Inject
    public KeyStoreApi15Impl(CustomPreference customPreference) {
        this.mCustomPreference = customPreference;
    }

    @Override
    public void init() {
    }

    @Override
    public boolean createKeys(@NonNull String alias) {
        if (mCustomPreference.retrieveEncryptedIv(alias) == null) {
            SecureRandom secureRandom = Utils.getRandom();

            KeyGenerator keyGenerator;
            try {
                keyGenerator = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            }

            keyGenerator.init(KEY_SIZE, secureRandom);
            byte[] key = (keyGenerator.generateKey()).getEncoded();

            mCustomPreference.storeEncryptedIv(key, alias);
        }

        return true;
    }

    @Nullable
    @Override
    public String encryptString(@NonNull String text, @NonNull String alias) {
        byte[] key = mCustomPreference.retrieveEncryptedIv(alias);

        if (key != null) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

                byte[] encrypted = cipher.doFinal(text.getBytes());

                return ByteArray.toHexString(encrypted);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }

        return text;
    }

    @Nullable
    @Override
    public String decryptString(@NonNull String text, @NonNull String alias) {
        byte[] key = mCustomPreference.retrieveEncryptedIv(alias);

        if (key != null) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

                byte[] encrypted = ByteArray.fromHexString(text);
                byte[] original = cipher.doFinal(encrypted);

                return new String(original);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }

        return text;
    }
}
