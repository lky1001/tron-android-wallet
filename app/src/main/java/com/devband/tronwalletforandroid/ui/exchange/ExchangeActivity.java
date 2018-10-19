package com.devband.tronwalletforandroid.ui.exchange;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeActivity extends CommonActivity implements ExchangeView {

    @Inject
    ExchangePresenter mExchangePresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_exchange);
        }

        mExchangePresenter.onCreate();
    }
}
