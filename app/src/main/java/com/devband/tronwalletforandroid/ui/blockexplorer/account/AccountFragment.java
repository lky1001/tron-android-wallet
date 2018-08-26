package com.devband.tronwalletforandroid.ui.blockexplorer.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.CommonFragment;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.blockexplorer.adapter.TronAccountAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 24..
 */

public class AccountFragment extends CommonFragment implements AccountView {

    private static final int PAGE_SIZE = 25;

    @Inject
    AccountPresenter mAccountPresenter;
    
    @BindView(R.id.recycler_view)
    RecyclerView mAccountListView;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private TronAccountAdapter mTronAccountAdapter;

    private long mStartIndex = 0;

    private boolean mIsLoading;

    private boolean mIsLastPage;

    public static Fragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        initUi();

        mAccountPresenter.setAdapterDataModel(mTronAccountAdapter);
        mAccountPresenter.onCreate();

        return view;
    }

    private void initUi() {
        mTronAccountAdapter = new TronAccountAdapter(getActivity(), mOnItemClickListener);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAccountListView.setLayoutManager(mLayoutManager);
        mAccountListView.addItemDecoration(new DividerItemDecoration(0));
        mAccountListView.setAdapter(mTronAccountAdapter);
        mAccountListView.addOnScrollListener(mRecyclerViewOnScrollListener);

        mAdapterView = mTronAccountAdapter;
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
                    mAccountPresenter.getTronAccounts(mStartIndex, PAGE_SIZE);
                }
            }
        }
    };

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void refresh() {
        if (!mIsLastPage && isAdded()) {
            mAccountPresenter.getTronAccounts(mStartIndex, PAGE_SIZE);
        }
    }

    @Override
    public void finishLoading(long total) {
        if (!isAdded()) {
            return;
        }
        mStartIndex += PAGE_SIZE;

        if (mStartIndex >= total) {
            mIsLastPage = true;
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
        mIsLoading = false;
        if (isAdded()) {
            hideDialog();
            Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
