package com.devband.tronwalletforandroid.ui.witness;

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
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.main.adapter.AdapterView;
import com.devband.tronwalletforandroid.ui.main.adapter.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.witness.adapter.WitnessListAdapter;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WitnessActivity extends CommonActivity implements WitnessView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar_layout)
    public AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.listview)
    RecyclerView mWitnessListView;

    @BindView(R.id.witness_title_text)
    TextView mWitnessTitleText;

    @BindView(R.id.witness_count_title_text)
    TextView mWitnessCountTitleText;

    @BindView(R.id.highest_votes_title_text)
    TextView mHighestVotesTitleText;

    @BindView(R.id.witness_count_text)
    TextView mWitnessCountText;

    @BindView(R.id.highest_votes_text)
    TextView mHighestVotesText;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private WitnessListAdapter mWitnessListAdapter;

    private DecimalFormat df = new DecimalFormat("#,##0");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witness);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mLayoutManager = new LinearLayoutManager(WitnessActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mWitnessListView.setLayoutManager(mLayoutManager);
        mWitnessListView.addItemDecoration(new DividerItemDecoration(0));
        mWitnessListView.setNestedScrollingEnabled(false);

        mWitnessListAdapter = new WitnessListAdapter(WitnessActivity.this);
        mWitnessListView.setAdapter(mWitnessListAdapter);
        mAdapterView = mWitnessListAdapter;

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mToolbarLayout.setTitle(getString(R.string.title_witness_list));
                    mWitnessTitleText.setVisibility(View.GONE);
                    mWitnessCountTitleText.setVisibility(View.GONE);
                    mHighestVotesTitleText.setVisibility(View.GONE);
                    mWitnessCountText.setVisibility(View.GONE);
                    mHighestVotesText.setVisibility(View.GONE);
                    isShow = true;
                } else if(isShow) {
                    mToolbarLayout.setTitle("");
                    mWitnessTitleText.setVisibility(View.VISIBLE);
                    mWitnessCountTitleText.setVisibility(View.VISIBLE);
                    mHighestVotesTitleText.setVisibility(View.VISIBLE);
                    mWitnessCountText.setVisibility(View.VISIBLE);
                    mHighestVotesText.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        mPresenter = new WitnessPresenter(this);
        ((WitnessPresenter) mPresenter).setAdapterDataModel(mWitnessListAdapter);
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
    public void displayWitnessInfo(int witnessCount, long highestVotes) {
        hideDialog();
        mWitnessCountText.setText(df.format(witnessCount));
        mHighestVotesText.setText(df.format(highestVotes) + " " + Constants.TRON_SYMBOL);

        mWitnessCountTitleText.setVisibility(View.VISIBLE);
        mHighestVotesTitleText.setVisibility(View.VISIBLE);
        mWitnessCountText.setVisibility(View.VISIBLE);
        mHighestVotesText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerError() {
        hideDialog();
        showProgressDialog(null, getString(R.string.connection_error_msg));
    }
}
