package com.devband.tronwalletforandroid.ui.token.transaction;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.ui.token.TokenDetailActivity;

import butterknife.ButterKnife;

public class TransactionFragment extends BaseFragment {

    private String mTokenName;

    public static BaseFragment newInstance(@NonNull String tokenName) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle(1);
        args.putString(TokenDetailActivity.EXTRA_TOKEN_NAME, tokenName);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_transaction, container, false);
        ButterKnife.bind(this, view);

        mTokenName = getArguments().getString(TokenDetailActivity.EXTRA_TOKEN_NAME);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void refresh() {

    }
}