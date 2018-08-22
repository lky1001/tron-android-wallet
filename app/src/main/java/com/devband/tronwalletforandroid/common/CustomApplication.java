package com.devband.tronwalletforandroid.common;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.devband.tronwalletforandroid.di.component.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.fabric.sdk.android.Fabric;

public class CustomApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder()
                .application(this)
                .build();
    }
}
