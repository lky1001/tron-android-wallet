package com.devband.tronwalletforandroid.ui.tronaccount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.tronaccount.adapter.TronAccountListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TronAccountActivity extends CommonActivity implements TronAccountView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar_layout)
    public AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.listview)
    RecyclerView mTronAccountListView;

    @BindView(R.id.account_title_text)
    TextView mAccountTitleText;

    @BindView(R.id.account_count_title_text)
    TextView mAccountCountTitleText;

    @BindView(R.id.highest_trx_title_text)
    TextView mHighestTrxTitleText;

    @BindView(R.id.account_count_text)
    TextView mAccountCountText;

    @BindView(R.id.highest_trx_text)
    TextView mHighestTrxText;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private TronAccountListAdapter mTronAccountListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tron_account);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mLayoutManager = new LinearLayoutManager(TronAccountActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTronAccountListView.setLayoutManager(mLayoutManager);
        mTronAccountListView.addItemDecoration(new DividerItemDecoration(0));
        mTronAccountListView.setNestedScrollingEnabled(false);

        mTronAccountListAdapter = new TronAccountListAdapter(TronAccountActivity.this);
        mTronAccountListView.setAdapter(mTronAccountListAdapter);
        mAdapterView = mTronAccountListAdapter;

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mToolbarLayout.setTitle(getString(R.string.title_tron_account_list));
                    mAccountTitleText.setVisibility(View.GONE);
                    mAccountCountTitleText.setVisibility(View.GONE);
                    mHighestTrxTitleText.setVisibility(View.GONE);
                    mAccountCountText.setVisibility(View.GONE);
                    mHighestTrxText.setVisibility(View.GONE);
                    isShow = true;
                } else if(isShow) {
                    mToolbarLayout.setTitle("");
                    mAccountTitleText.setVisibility(View.VISIBLE);
                    mAccountCountTitleText.setVisibility(View.VISIBLE);
                    mHighestTrxTitleText.setVisibility(View.VISIBLE);
                    mAccountCountText.setVisibility(View.VISIBLE);
                    mHighestTrxText.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        mPresenter = new TronAccountPresenter(this);
        ((TronAccountPresenter) mPresenter).setAdapterDataModel(mTronAccountListAdapter);
        mPresenter.onCreate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void displayTronAccountInfo(int tronAccountCount, long highestTrx) {
        mAccountCountText.setText(Constants.numberFormat.format(tronAccountCount));
        mHighestTrxText.setText(Constants.tronBalanceFormat.format(highestTrx / Constants.ONE_TRX) + " " + Constants.TRON_SYMBOL);

        mAccountCountTitleText.setVisibility(View.VISIBLE);
        mHighestTrxTitleText.setVisibility(View.VISIBLE);
        mAccountCountText.setVisibility(View.VISIBLE);
        mHighestTrxText.setVisibility(View.VISIBLE);
        hideDialog();
    }

    @Override
    public void showServerError() {
        hideDialog();
        showProgressDialog(null, getString(R.string.connection_error_msg));
    }
}
