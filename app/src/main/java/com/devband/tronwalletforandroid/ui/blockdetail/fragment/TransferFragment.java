package com.devband.tronwalletforandroid.ui.blockdetail.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.ui.blockdetail.BlockDetailActivity;

public class TransferFragment extends BaseFragment {

    public static BaseFragment newInstance(@NonNull long blockNumber) {
        TransferFragment fragment = new TransferFragment();
        Bundle args = new Bundle(1);
        args.putLong(BlockDetailActivity.EXTRA_BLOCK_NUMBER, blockNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void refresh() {

    }
}
