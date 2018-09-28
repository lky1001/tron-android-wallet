package com.devband.tronwalletforandroid.ui.node;

import com.devband.tronwalletforandroid.rxjava.RxJavaSchedulers;
import com.devband.tronwalletforandroid.tron.Tron;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class NodeActivityModule {

    @Binds
    public abstract NodeView view(NodeActivity nodeActivity);

    @Provides
    static NodePresenter provideNodePresenter(NodeView view, Tron tron, RxJavaSchedulers rxJavaSchedulers) {
        return new NodePresenter(view, tron, rxJavaSchedulers);
    }
}
