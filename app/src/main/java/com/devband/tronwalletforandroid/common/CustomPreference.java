package com.devband.tronwalletforandroid.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomPreference {

    private static final String CUSTOM_PREFERENCE = "custom_preference";
    private static final String PREFERENCE_SETTINGS = "preference_settings";
    public static final String FAVORITE_TOKENS_KEY_PREFIX = "favorite_tokens_";

    private final String LOG_TAG = CustomPreference.class.getSimpleName();

    private final SharedPreferences mSharedPreferences;

    private Context mContext;

    private TronSettings mSettings;

    public CustomPreference(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(CUSTOM_PREFERENCE, Activity.MODE_PRIVATE);
        loadSettings();
    }

    private void loadSettings() {
        ObjectMapper mapper = new ObjectMapper();

        String data = mSharedPreferences.getString(PREFERENCE_SETTINGS, null);

        if (!TextUtils.isEmpty(data)) {
            try {
                mSettings = mapper.readValue(data, TronSettings.class);
            } catch (IOException e) {
                mSettings = new TronSettings();
            }
        } else {
            mSettings = new TronSettings();
        }
    }

    public void setCustomFullNodeHost(String host) {
        mSettings.fullNodeHost = host;
        saveSettings();
    }

    public String getCustomFullNodeHost() {
        return mSettings.fullNodeHost;
    }

    public void setUseFingerprint(boolean isUse) {
        mSettings.useFingerprint = isUse;
        saveSettings();
    }

    public boolean getUseFingerprint() {
        return mSettings.useFingerprint;
    }

    public void setFavoriteToken(int accountId, boolean isFavorite) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(FAVORITE_TOKENS_KEY_PREFIX + accountId, isFavorite);
        edit.apply();
    }

    public boolean isFavoriteToken(int accountId) {
        return mSharedPreferences.getBoolean(FAVORITE_TOKENS_KEY_PREFIX + accountId, false);
    }

    public void storeEncryptedIv(byte[] data, String name) {
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString(name, base64);
        edit.apply();
    }

    public byte[] retrieveEncryptedIv(String name) {
        String base64 = mSharedPreferences.getString(name, null);
        if (base64 == null) return null;
        return Base64.decode(base64, Base64.DEFAULT);
    }

    public void destroyEncryptedIv(String name) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.remove(name);
        edit.apply();
    }

    public void saveSettings() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper.writeValueAsString(mSettings);

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(PREFERENCE_SETTINGS, data);
            editor.apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void setInitWallet(boolean isInit) {
        this.mSettings.initWallet = isInit;
        saveSettings();
    }

    public boolean getInitWallet() {
        return mSettings.initWallet;
    }

    public void setKeyStoreVersion(int keyStoreVersion) {
        mSettings.keyStoreVersion = keyStoreVersion;
        saveSettings();
    }

    public int getKeyStoreVersion() {
        return mSettings.keyStoreVersion;
    }

    public String getSalt() {
        return this.mSettings.salt;
    }

    public void setSalt(String salt) {
        this.mSettings.salt = salt;
        saveSettings();
    }

    public void setMigrationDb(boolean migrationDb) {
        this.mSettings.migrationDb = migrationDb;
        saveSettings();
    }

    public boolean getMigrationDb() {
        return this.mSettings.migrationDb;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TronSettings {

        public String fullNodeHost;
        public boolean useFingerprint;
        public String salt;
        public boolean initWallet;
        public int keyStoreVersion;
        public boolean migrationDb;
        public Map<Integer, Boolean> favoritesToken;
    }
}
