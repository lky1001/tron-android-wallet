package com.devband.tronwalletforandroid.ui.accountdetail.overview;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonFragment;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OverviewFragment extends CommonFragment implements OverviewView {

    @Inject
    OverviewPresenter mOverviewPresenter;

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
    LinearLayout mTokensLayout;

    private String mAddress;

    public static Fragment newInstance(@NonNull String address) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle(1);
        args.putString(AccountDetailActivity.EXTRA_ADDRESS, address);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_overview, container, false);
        ButterKnife.bind(this, view);

        mAddress = getArguments().getString(AccountDetailActivity.EXTRA_ADDRESS);

        mOverviewPresenter.onCreate();

        mOverviewPresenter.getAccount(mAddress);

        return view;
    }

    @Override
    protected void refresh() {
        if (isAdded()) {
            mOverviewPresenter.getAccount(mAddress);
        }
    }

    @Override
    public void showLoadingDialog() {
        if (isAdded()) {
            showProgressDialog(null, getString(R.string.loading_msg));
        }
    }

    @Override
    public void showServerError() {
        if (isAdded()) {
            hideDialog();
            Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finishLoading(@NonNull TronAccount account) {
        if (!isAdded()) {
            return;
        }

        mAddressText.setText(account.getAccount().getAddress());

        if (!TextUtils.isEmpty(account.getAccount().getName())) {
            mAccountNameText.setText(account.getAccount().getName());
        } else {
            mAccountNameText.setText("-");
        }
        mBalanceText.setText(Constants.tokenBalanceFormat.format(account.getBalance() / Constants.ONE_TRX)
                + " " + Constants.TRON_SYMBOL);
        mTronPowerText.setText(Constants.numberFormat.format(account.getBandwidth()));
        mTransactionInText.setText(Constants.numberFormat.format(account.getTransactionIn()));
        mTransactionOutText.setText(Constants.numberFormat.format(account.getTransactionOut()));

        if (account.getAccount().getRepresentative().isEnabled()) {
            mAccountTypeText.setText(getString(R.string.representative_text));
        } else {
            mAccountTypeText.setText(getString(R.string.normal_text));
            ((AccountDetailActivity) getActivity()).removeRepresentativeMenu();
        }

        mTokensLayout.removeAllViews();

        if (!account.getAssetList().isEmpty()) {
            for (Asset asset : account.getAssetList()) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.list_item_my_token, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT));

                TextView tokenNameText = v.findViewById(R.id.token_name_text);
                TextView tokenAmountText = v.findViewById(R.id.token_amount_text);

                tokenNameText.setText(asset.getName());
                tokenAmountText.setText(Constants.tokenBalanceFormat.format(asset.getBalance()));
                mTokensLayout.addView(v);
            }
        } else {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.list_item_my_token, null);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));

            TextView tokenNameText = v.findViewById(R.id.token_name_text);
            TextView tokenAmountText = v.findViewById(R.id.token_amount_text);

            tokenNameText.setText(getString(R.string.no_tokens));
            tokenNameText.setGravity(Gravity.CENTER);
            tokenAmountText.setVisibility(View.GONE);
            mTokensLayout.addView(v);
        }

        hideDialog();
    }

    @OnClick(R.id.copy_address)
    public void onCopyAddressClick() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", mAddress);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), getString(R.string.copy_address_msg), Toast.LENGTH_SHORT)
                .show();
    }

    @OnClick(R.id.view_votes_button)
    public void onViewVotesClick() {
        Intent intent = new Intent(getActivity(), AccountVoteActivity.class);
        intent.putExtra(AccountDetailActivity.EXTRA_ADDRESS, mAddress);

        startActivity(intent);
    }
}

