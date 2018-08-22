package com.devband.tronwalletforandroid.di;

import com.devband.tronwalletforandroid.ui.intro.IntroActivity;
import com.devband.tronwalletforandroid.ui.intro.IntroActivityModule;

import dagger.Module;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;

@Module(includes = AndroidInjectionModule.class)
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = { IntroActivityModule.class })
    abstract IntroActivity bindIntroActivity();
}
