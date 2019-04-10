package com.devband.tronwalletforandroid.ui.balancecheck;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BalanceCheckingActivity extends CommonActivity implements BalanceCheckingView {

    @Inject
    BalanceCheckingPresenter balanceCheckingPresenter;

    @BindView(R.id.check_listview)
    public RecyclerView recyclerView;

    @BindView(R.id.fab_add)
    public FloatingActionButton addButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_checking);

        ButterKnife.bind(this);
    }
}
