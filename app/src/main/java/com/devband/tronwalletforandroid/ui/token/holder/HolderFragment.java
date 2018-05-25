package com.devband.tronwalletforandroid.ui.token.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;

import butterknife.ButterKnife;

public class HolderFragment extends BaseFragment {
    public static BaseFragment newInstance() {
        HolderFragment fragment = new HolderFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_holder, container, false);
        ButterKnife.bind(this, view);
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