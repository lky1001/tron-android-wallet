package com.devband.tronwalletforandroid.ui.vote;

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
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.vote.adapter.VoteListAdapter;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteActivity extends CommonActivity implements VoteView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar_layout)
    public AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.listview)
    RecyclerView mVoteListView;

    @BindView(R.id.representative_title_text)
    TextView mRepresentativeTitleText;

    @BindView(R.id.representative_count_title_text)
    TextView mRepresentativeCountTitleText;

    @BindView(R.id.total_votes_title_text)
    TextView mTotalVotesTitleText;

    @BindView(R.id.representative_count_text)
    TextView mRepresentativeCountText;

    @BindView(R.id.total_votes_text)
    TextView mTotalVotesText;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private VoteListAdapter mVoteListAdapter;

    private DecimalFormat df = new DecimalFormat("#,##0");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mLayoutManager = new LinearLayoutManager(VoteActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVoteListView.setLayoutManager(mLayoutManager);
        mVoteListView.addItemDecoration(new DividerItemDecoration(0));
        mVoteListView.setNestedScrollingEnabled(false);

        mVoteListAdapter = new VoteListAdapter(VoteActivity.this, null);
        mVoteListView.setAdapter(mVoteListAdapter);
        mAdapterView = mVoteListAdapter;

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mToolbarLayout.setTitle(getString(R.string.title_vote));
                    mRepresentativeTitleText.setVisibility(View.GONE);
                    mRepresentativeCountTitleText.setVisibility(View.GONE);
                    mTotalVotesTitleText.setVisibility(View.GONE);
                    mRepresentativeCountText.setVisibility(View.GONE);
                    mTotalVotesText.setVisibility(View.GONE);
                    isShow = true;
                } else if(isShow) {
                    mToolbarLayout.setTitle("");
                    mRepresentativeTitleText.setVisibility(View.VISIBLE);
                    mRepresentativeCountTitleText.setVisibility(View.VISIBLE);
                    mTotalVotesTitleText.setVisibility(View.VISIBLE);
                    mRepresentativeCountText.setVisibility(View.VISIBLE);
                    mTotalVotesText.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        mPresenter = new VotePresenter(this);
        ((VotePresenter) mPresenter).setAdapterDataModel(mVoteListAdapter);
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
    public void showServerError() {
        hideDialog();
        Toast.makeText(VoteActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayVoteInfo(long totalVotes, long voteItemCount, long myVotePoint, long totalMyVotes) {
        mTotalVotesText.setText(df.format(totalVotes));
        mRepresentativeCountTitleText.setText(df.format(voteItemCount));

        mRepresentativeTitleText.setVisibility(View.VISIBLE);
        mRepresentativeCountTitleText.setVisibility(View.VISIBLE);
        mTotalVotesTitleText.setVisibility(View.VISIBLE);
        mRepresentativeCountText.setVisibility(View.VISIBLE);
        mTotalVotesText.setVisibility(View.VISIBLE);

        hideDialog();
    }
}
