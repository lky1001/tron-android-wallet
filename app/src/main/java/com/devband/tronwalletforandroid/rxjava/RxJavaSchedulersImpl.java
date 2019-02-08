package com.devband.tronwalletforandroid.rxjava;

import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class RxJavaSchedulersImpl implements RxJavaSchedulers {

    @Override
    public Scheduler getNewThread() {
        return Schedulers.newThread();
    }

    @Override
    public Scheduler getComputation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler getIo() {
        return Schedulers.io();
    }

    @Override
    public Scheduler getMainThread() {
        return AndroidSchedulers.mainThread();
    }
}
