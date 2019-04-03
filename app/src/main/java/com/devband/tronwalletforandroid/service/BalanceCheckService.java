package com.devband.tronwalletforandroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BalanceCheckService extends Service {

    @MainThread
    public static void startService(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, BalanceCheckService.class));
        } else {
            context.startService(new Intent(context, BalanceCheckService.class));
        }
    }

    @MainThread
    public static void stopService(@NonNull Context context) {
        context.stopService(new Intent(context, BalanceCheckService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
