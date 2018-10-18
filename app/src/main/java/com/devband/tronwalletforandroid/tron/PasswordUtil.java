package com.devband.tronwalletforandroid.tron;

import android.support.annotation.NonNull;

import org.tron.common.crypto.Hash;
import org.tron.common.utils.ByteArray;

import java.util.Arrays;

public class PasswordUtil {

    private static final int KEY_SIZE = 16;

    @NonNull
    public static String getHashedPassword(@NonNull String password) {
        byte[] pwd;
        pwd = Hash.sha256(password.getBytes());
        pwd = Hash.sha256(pwd);
        pwd = Arrays.copyOfRange(pwd, 0, KEY_SIZE);

        return ByteArray.toHexString(pwd);
    }

    public static boolean matches(@NonNull String rawPassword, @NonNull String hashedPassword) {
        String rawHashedPassword = getHashedPassword(rawPassword);

        if (rawHashedPassword.equals(hashedPassword)) {
            return true;
        }

        return false;
    }
}