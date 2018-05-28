package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronlib.dto.Account;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewFragment extends BaseFragment implements OverviewView {

    public static BaseFragment newInstance(@NonNull String address) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle(1);
        args.putString(AccountDetailActivity.EXTRA_ADDRESS, address);

        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.address_text)
    TextView mAddressText;

    @BindView(R.id.account_name_text)
    TextView mAccountNameText;

    @BindView(R.id.account_type_text)
    TextView mAccountTypeText;

    @BindView(R.id.balance_text)
    TextView mBalanceText;

    @BindView(R.id.tron_power_text)
    TextView mTronPowerText;

    @BindView(R.id.transaction_out_text)
    TextView mTransactionOutText;

    @BindView(R.id.transaction_in_text)
    TextView mTransactionInText;

    @BindView(R.id.tokens_layout)
    LinearLayout mTokenLayout;

    @BindView(R.id.votes_layout)
    LinearLayout mVoteLayout;

    private String mAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_overview, container, false);
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
        ((OverviewPresenter) mPresenter).getAccount(mAddress);
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

    @Override
    public void finishLoading(@NonNull Account account) {

        hideDialog();
    }
}

