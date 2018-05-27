package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;

import butterknife.ButterKnife;

public class OverviewFragment extends BaseFragment implements OverviewView {

    private String mAddress;

    public static BaseFragment newInstance(@NonNull String tokenName) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle(1);
        args.putString(AccountDetailActivity.EXTRA_ADDRESS, tokenName);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_transaction, container, false);
        ButterKnife.bind(this, view);

        mAddress = getArguments().getString(AccountDetailActivity.EXTRA_ADDRESS);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void refresh() {

    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                Toast.LENGTH_SHORT).show();
    }
}

