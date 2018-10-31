package com.devband.tronwalletforandroid.ui.exchange;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeActivity extends CommonActivity implements ExchangeView {

    @Inject
    ExchangePresenter mExchangePresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    View mEmptyView;

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_exchange);
        }

        mSearchView.setOnQueryTextListener(mOnQueryTextListener);
        mSearchView.setOnSearchViewListener(mOnSearchViewListener);

        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        mLayoutManager = new LinearLayoutManager(ExchangeActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mExchangePresenter.onCreate();
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            mSwipeRefreshLayout.setRefreshing(true);
            mSearchView.closeSearch();
        }
    };

    private MaterialSearchView.OnQueryTextListener mOnQueryTextListener = new MaterialSearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mSearchView.closeSearch();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private MaterialSearchView.SearchViewListener mOnSearchViewListener = new MaterialSearchView.SearchViewListener() {

        @Override
        public void onSearchViewShown() {

        }

        @Override
        public void onSearchViewClosed() {

        }
    };

}
