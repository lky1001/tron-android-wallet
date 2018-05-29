package com.devband.tronwalletforandroid.ui.blockdetail.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.ui.blockdetail.BlockDetailActivity;

public class TransactionFragment extends BaseFragment {

    public static BaseFragment newInstance(@NonNull long blockNumber) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle(1);
        args.putLong(BlockDetailActivity.EXTRA_BLOCK_NUMBER, blockNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void refresh() {

    }
}
