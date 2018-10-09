package com.devband.tronwalletforandroid.common.security;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.common.security.keystore.KeyStore;

public class PasswordEncoderImpl implements PasswordEncoder {

    private static final int SALT_LOG_ROUND = 31;
    private static final String ALIAS_SALT = "alias_salt";

    private CustomPreference customPreference;
    private KeyStore keyStore;
    private UpdatableBCrypt updatableBCrypt;

    public PasswordEncoderImpl(CustomPreference customPreference, KeyStore keyStore) {
        this.customPreference = customPreference;
        this.keyStore = keyStore;
        this.updatableBCrypt = new UpdatableBCrypt(SALT_LOG_ROUND);
        init();
    }

    private void init() {
        if (!keyStore.createKeys(ALIAS_SALT)) {
            // todo
            //throw new NoSuchAlgorithmException();
        }

        if (!TextUtils.isEmpty(this.customPreference.getSalt())) {
            String salt = this.updatableBCrypt.gensalt();

            this.customPreference.setSalt(this.keyStore.encryptString(salt, ALIAS_SALT));
        };
    }

    @Override
    public String encode(@NonNull String rawPassword) {
        return this.updatableBCrypt.hash(rawPassword, this.keyStore.decryptString(this.customPreference.getSalt(), ALIAS_SALT));
    }

    @Override
    public boolean matches(@NonNull String rawPassword, @NonNull String encodedPassword) {
        return this.updatableBCrypt.verifyHash(rawPassword, encodedPassword);
    }
}
