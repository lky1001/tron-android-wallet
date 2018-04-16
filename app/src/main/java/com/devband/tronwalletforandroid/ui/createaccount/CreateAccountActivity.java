package com.devband.tronwalletforandroid.ui.createaccount;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

public class CreateAccountActivity extends CommonActivity implements CreateAccountView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mPresenter = new CreateAccountPresenter(this);
        mPresenter.onCreate();
    }
}
