package com.devband.tronwalletforandroid.rxjava;

import javax.inject.Singleton;

import io.reactivex.Scheduler;

@Singleton
public interface RxJavaSchedulers {

    Scheduler getNewThread();

    Scheduler getComputation();

    Scheduler getIo();

    Scheduler getMainThread();
}
