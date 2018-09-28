package com.devband.tronwalletforandroid.ui.token.holder;

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
import com.devband.tronwalletforandroid.ui.token.TokenDetailActivity;
import com.devband.tronwalletforandroid.ui.token.adapter.HolderAdapter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HolderFragment extends CommonFragment implements HolderView {

    private static final int PAGE_SIZE = 25;
    
    @Inject
    HolderPresenter mHolderPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mHolderListView;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private HolderAdapter mHolderAdapter;

    private String mTokenName;

    private long mStartIndex = 0;

    private boolean mIsLoading;

    private boolean mIsLastPage;

    public static Fragment newInstance(@NonNull String tokenName) {
        HolderFragment fragment = new HolderFragment();
        Bundle args = new Bundle(1);
        args.putString(TokenDetailActivity.EXTRA_TOKEN_NAME, tokenName);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_holder, container, false);
        ButterKnife.bind(this, view);

        mTokenName = getArguments().getString(TokenDetailActivity.EXTRA_TOKEN_NAME);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHolderListView.setLayoutManager(mLayoutManager);
        mHolderListView.addItemDecoration(new DividerItemDecoration(0));
        mHolderListView.addOnScrollListener(mRecyclerViewOnScrollListener);

        mHolderAdapter = new HolderAdapter(getActivity(), mOnItemClickListener);
        mHolderListView.setAdapter(mHolderAdapter);
        mAdapterView = mHolderAdapter;

        mHolderPresenter.setAdapterDataModel(mHolderAdapter);
        mHolderPresenter.onCreate();
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
                    mHolderPresenter.getTokenHolders(mTokenName, mStartIndex, PAGE_SIZE);
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
        if (isAdded()) {
            mHolderPresenter.getTokenHolders(mTokenName, mStartIndex, PAGE_SIZE);
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