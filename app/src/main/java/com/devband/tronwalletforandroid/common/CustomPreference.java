package com.devband.tronwalletforandroid.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomPreference {

    private static final String CUSTOM_PREFERENCE = "custom_preference";
    private static final String PREFERENCE_SETTINGS = "preference_settings";

    private final String LOG_TAG = CustomPreference.class.getSimpleName();
    private static volatile CustomPreference mInstance;

    private Context mContext;

    private TronSettings mSettings;

    private CustomPreference() {}

    private CustomPreference(Context context) {
        mContext = context;
        loadSettings();
    }

    public static CustomPreference getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CustomPreference.class) {
                if (mInstance == null) {
                    mInstance = new CustomPreference(context);
                }
            }
        }

        return mInstance;
    }

    private void loadSettings() {
        SharedPreferences pref = mContext.getSharedPreferences(CUSTOM_PREFERENCE, Activity.MODE_PRIVATE);
        ObjectMapper mapper = new ObjectMapper();

        String data = pref.getString(PREFERENCE_SETTINGS, null);

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

    public void saveSettings() {
        SharedPreferences pref = mContext.getSharedPreferences(CUSTOM_PREFERENCE, Activity.MODE_PRIVATE);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String data = mapper.writeValueAsString(mSettings);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString(PREFERENCE_SETTINGS, data);
            editor.apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TronSettings {

        public boolean autoLogin;
        public String address;
    }
}
