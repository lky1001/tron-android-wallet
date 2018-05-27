package com.devband.tronwalletforandroid.ui.accountdetail.transaction;

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
import com.devband.tronwalletforandroid.ui.accountdetail.adapter.AccountTransactionAdapter;
import com.devband.tronwalletforandroid.ui.transaction.TransactionPresenter;
import com.devband.tronwalletforandroid.ui.transaction.TransactionView;
import com.devband.tronwalletforandroid.ui.transaction.dto.TransactionInfo;

import java.util.List;

import butterknife.ButterKnife;

public class TransactionFragment extends BaseFragment implements TransactionView {

    private String mAddress;

    private AccountTransactionAdapter mAdapter;

    public static BaseFragment newInstance(@NonNull String tokenName) {
        TransactionFragment fragment = new TransactionFragment();
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

        mPresenter = new TransactionPresenter(this);
        mPresenter.onCreate();

        return view;
    }

    @Override
    protected void refresh() {
    }

    @Override
    public void transactionDataLoadSuccess(List<TransactionInfo> transactionInfos) {
        hideDialog();
        if (mAdapter != null) {
            mAdapter.refresh(transactionInfos);
        }
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
