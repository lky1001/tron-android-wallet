package com.devband.tronwalletforandroid.ui.vote;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import butterknife.BindView;

public class VoteActivity extends CommonActivity implements VoteView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar_layout)
    public AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.listview)
    RecyclerView mVoteListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(VoteActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }
}
