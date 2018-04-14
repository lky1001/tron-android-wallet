package com.devband.tronwalletforandroid.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

public class IntroActivity extends CommonActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // todo - app init

        Single.fromCallable(() -> true)
                .delay(3, TimeUnit.SECONDS)
                .subscribe(result -> {
                    if (result) {
                        startActivity(MainActivity.class);
                        finish();
                    } else {
                        // todo - failed app init
                    }
                }, e -> {

                });
    }
}
