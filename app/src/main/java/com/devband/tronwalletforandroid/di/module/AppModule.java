package com.devband.tronwalletforandroid.di.module;

import android.app.Application;
import android.content.Context;

import com.devband.tronwalletforandroid.di.ApplicationContext;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppModule {

    @Binds
    @ApplicationContext
    abstract Context bindContext(Application application);
}
