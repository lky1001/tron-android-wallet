package com.devband.tronwalletforandroid.ui.block;

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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 24..
 */

public class BlockFragment extends BaseFragment implements BlockView {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private BlockAdapter mBlockAdapter;


    static BaseFragment newInstance() {
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mBlockAdapter);
    }

    @Override
    protected void refresh() {
        ((BlockPresenter) mPresenter).loadBlockData();
    }

    @Override
    public void blockDataLoadSuccess(Blocks blocks, boolean added) {
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
