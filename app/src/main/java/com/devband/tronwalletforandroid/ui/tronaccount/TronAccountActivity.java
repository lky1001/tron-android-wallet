package com.devband.tronwalletforandroid.ui.tronaccount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TronAccountActivity extends CommonActivity implements TronAccountView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tron_account);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_account_list);
        }

        mPresenter = new TronAccountPresenter(this);
        mPresenter.onCreate();
    }
}
