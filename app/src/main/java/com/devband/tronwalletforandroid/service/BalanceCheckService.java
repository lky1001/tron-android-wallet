package com.devband.tronwalletforandroid.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;

public class BalanceCheckService extends Service {

    private static final String CHANNEL_ID = "tron_balance_check_channel";

    private static boolean isStart;

    private static Tron TRON;
    private CustomPreference customPreference;

    @MainThread
    public static void startService(@NonNull Context context, Tron tron) {
        TRON = tron;

        Intent notificationIntent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Tron Wallet",
                    NotificationManager.IMPORTANCE_DEFAULT);


            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

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
        if (customPreference == null) {
            customPreference = new CustomPreference(getApplicationContext());
        }

        long interval = customPreference.getBalanceCheckIntervalInMs();

        while (isStart) {

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
