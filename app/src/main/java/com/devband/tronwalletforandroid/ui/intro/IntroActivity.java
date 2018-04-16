package com.devband.tronwalletforandroid.ui.intro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.activities.MainActivity;
import com.devband.tronwalletforandroid.common.CommonActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

public class IntroActivity extends CommonActivity implements IntroView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mPresenter = new IntroPresenter(this);
        mPresenter.onCreate();
    }

    @Override
    public void startMainActivity() {
        startActivity(MainActivity.class);
        finish();
    }
}
