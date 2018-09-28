package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.tronwalletforandroid.ui.accountdetail.adapter.VoteAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountVoteActivity extends CommonActivity implements AccountVoteView {

    @Inject
    AccountVotePresenter mAccountVotePresenter;

    private String mAddress;

    private static final int PAGE_SIZE = 25;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.no_votes_text)
    TextView mNoVotesText;

    @BindView(R.id.recycler_view)
    RecyclerView mListView;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private VoteAdapter mAdapter;

    private long mStartIndex = 0;

    private boolean mIsLoading;

    private boolean mIsLastPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAddress = getIntent().getStringExtra(AccountDetailActivity.EXTRA_ADDRESS);

        if (TextUtils.isEmpty(mAddress)) {
            finish();
        }

        setContentView(R.layout.activity_account_votes);

        ButterKnife.bind(this);
        initUi();

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.vote_title));
        }

        mAccountVotePresenter.setAdapterDataModel(mAdapter);
        mAccountVotePresenter.onCreate();

        mAccountVotePresenter.getVotes(mAddress, mStartIndex, PAGE_SIZE);
    }

    private void initUi() {
        mAdapter = new VoteAdapter(this, mOnItemClickListener);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mListView.setLayoutManager(mLayoutManager);
        mListView.addItemDecoration(new DividerItemDecoration(0));
        mListView.setAdapter(mAdapter);
        mListView.addOnScrollListener(mRecyclerViewOnScrollListener);

        mAdapterView = mAdapter;
    }

    private RecyclerView.OnScrollListener mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!mIsLoading && !mIsLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    mIsLoading = true;
                    mAccountVotePresenter.getVotes(mAddress, mStartIndex, PAGE_SIZE);
                }
            }
        }
    };

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //int pos = mListView.getChildLayoutPosition(v);
            //Transaction item = mAdapter.getItem(pos);
            // todo
        }
    };

    @Override
    public void finishLoading(long totalVotes, long total) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.vote_title) + " (" + Constants.numberFormat.format(totalVotes) + ")");
        }

        mStartIndex += PAGE_SIZE;

        if (mStartIndex >= total) {
            mIsLastPage = true;
        }

        if (total == 0) {
            mNoVotesText.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mNoVotesText.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

        mIsLoading = false;
        mAdapterView.refresh();

        hideDialog();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        if (!isFinishing()) {
            hideDialog();
            Toast.makeText(this, getString(R.string.connection_error_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
