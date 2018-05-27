package com.devband.tronwalletforandroid.ui.blockexplorer.block;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devband.tronlib.dto.Blocks;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.ui.blockexplorer.adapter.BlockAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 24..
 */

public class BlockFragment extends BaseFragment implements BlockView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private BlockAdapter mBlockAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mDataLoading = false;

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

            if (!mDataLoading) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    mDataLoading = true;
                    ((BlockPresenter) mPresenter).loadBlockData();
                }
            }
        }
    };


    public static BaseFragment newInstance() {
        BlockFragment fragment = new BlockFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block, container, false);
        ButterKnife.bind(this, view);
        initUi();
        mPresenter = new BlockPresenter(this);
        return view;
    }

    private void initUi() {
        mBlockAdapter = new BlockAdapter();
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mBlockAdapter);
        mRecyclerView.addOnScrollListener(mRecyclerViewOnScrollListener);
    }

    @Override
    protected void refresh() {
        ((BlockPresenter) mPresenter).loadBlockData();
    }

    @Override
    public void blockDataLoadSuccess(Blocks blocks, boolean added) {
        mDataLoading = false;
        hideDialog();
        if (added) {
            mBlockAdapter.addData(blocks);
        } else {
            mBlockAdapter.refresh(blocks);
        }

    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, null);
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(getActivity(), getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }
}
