package com.devband.tronwalletforandroid.common.security.keystore;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.inject.Singleton;
import javax.security.auth.x500.X500Principal;

@Singleton
public class KeyStoreApi18Impl implements KeyStore {

    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    private java.security.KeyStore mKeyStore;

    private Context mContext;

    public KeyStoreApi18Impl(Context context) {
        this.mContext = context;
    }

    @Override
    public void init() {
        try {
            mKeyStore = java.security.KeyStore.getInstance(ANDROID_KEY_STORE);
            mKeyStore.load(null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean createKeys(@NonNull String alias) {
        if (mKeyStore == null) {
            return false;
        }

        try {
            if (!mKeyStore.containsAlias(alias)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, KEY_EXPIRED_IN_YEAR);

                KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);
                generator.initialize(new KeyPairGeneratorSpec.Builder(mContext)
                        .setAlias(alias)
                        .setSubject(new X500Principal("CN=Eos Pocket, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build());
                generator.generateKeyPair();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Nullable
    @Override
    public String encryptString(@NonNull String text, @NonNull String alias) {
        if (mKeyStore == null) {
            init();
        }

        byte [] values = null;

        try {
            java.security.KeyStore.PrivateKeyEntry privateKeyEntry = (java.security.KeyStore.PrivateKeyEntry) mKeyStore.getEntry(alias, null);

            Cipher inCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            inCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(
                    outputStream, inCipher);
            cipherOutputStream.write(text.getBytes("UTF-8"));
            cipherOutputStream.close();

            values = outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (values == null) {
            return null;
        }

        return Base64.encodeToString(values, Base64.DEFAULT);
    }

    @Nullable
    @Override
    public String decryptString(@NonNull String text, @NonNull String alias) {
        if (mKeyStore == null) {
            init();
        }

        String results = null;

        try {
            java.security.KeyStore.PrivateKeyEntry privateKeyEntry = (java.security.KeyStore.PrivateKeyEntry)mKeyStore.getEntry(alias, null);

            Cipher output = Cipher.getInstance(CIPHER_ALGORITHM);
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(text, Base64.DEFAULT)), output);

            ArrayList<Byte> values = new ArrayList<>();

            int nextByte;

            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];

            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i);
            }

            results = new String(bytes, 0, bytes.length, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  results;
    }
}
