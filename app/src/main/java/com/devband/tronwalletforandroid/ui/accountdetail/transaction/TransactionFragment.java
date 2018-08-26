package com.devband.tronwalletforandroid.ui.accountdetail.transaction;

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
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.tronwalletforandroid.ui.accountdetail.adapter.AccountTransactionAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionFragment extends CommonFragment implements TransactionView {

    @Inject
    TransactionPresenter mTransactionPresenter;
    
    private String mAddress;
    private long mBlock;

    private static final int PAGE_SIZE = 25;

    @BindView(R.id.recycler_view)
    RecyclerView mListView;

    @BindView(R.id.empty_view)
    View mEmptyView;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private AccountTransactionAdapter mAdapter;

    private long mStartIndex = 0;

    private boolean mIsLoading;

    private boolean mIsLastPage;

    public static Fragment newInstance(@NonNull String address) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle(1);
        args.putString(AccountDetailActivity.EXTRA_ADDRESS, address);

        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(@NonNull long block) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle(1);
        args.putLong(AccountDetailActivity.EXTRA_BLOCK, block);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        ButterKnife.bind(this, view);
        initUi();

        mAddress = getArguments().getString(AccountDetailActivity.EXTRA_ADDRESS);
        mBlock = getArguments().getLong(AccountDetailActivity.EXTRA_BLOCK, 0L);

        mTransactionPresenter.setAdapterDataModel(mAdapter);
        mTransactionPresenter.onCreate();

        return view;
    }

    private void initUi() {
        mAdapter = new AccountTransactionAdapter(getActivity(), mOnItemClickListener, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
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

                    if (mBlock > 0L) {
                        mTransactionPresenter.getTransactions(mBlock, mStartIndex, PAGE_SIZE);
                    } else {
                        mTransactionPresenter.getTransactions(mAddress, mStartIndex, PAGE_SIZE);
                    }
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
    protected void refresh() {
        if (!mIsLastPage && isAdded()) {
            if (mBlock > 0L) {
                mTransactionPresenter.getTransactions(mBlock, mStartIndex, PAGE_SIZE);
            } else {
                mTransactionPresenter.getTransactions(mAddress, mStartIndex, PAGE_SIZE);
            }
        }
    }

    @Override
    public void finishLoading(long total) {
        if (!isAdded()) {
            return;
        }
        boolean isFirstLoading = mStartIndex == 0;

        mStartIndex += PAGE_SIZE;

        if (mStartIndex >= total) {
            mIsLastPage = true;
        }

        mIsLoading = false;
        mAdapterView.refresh();

        if (isFirstLoading && total == 0) {
            mListView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);

        } else {
            if (mListView.getVisibility() != View.VISIBLE) {
                mListView.setVisibility(View.VISIBLE);
            }
            if (mEmptyView.getVisibility() == View.VISIBLE) {
                mEmptyView.setVisibility(View.GONE);
            }
        }

        hideDialog();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        if (isAdded()) {
            hideDialog();
            Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
