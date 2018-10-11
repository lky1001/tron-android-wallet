package com.devband.tronwalletforandroid.common.security.keystore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class KeyStoreApi15Impl implements KeyStore {

    // todo - implements

    @Override
    public void init() {

    }

    @Override
    public boolean createKeys(@NonNull String alias) {
        return true;
    }

    @Nullable
    @Override
    public String encryptString(@NonNull String text, @NonNull String alias) {
        return text;
    }

    @Nullable
    @Override
    public String decryptString(@NonNull String text, @NonNull String alias) {
        return text;
    }
}
