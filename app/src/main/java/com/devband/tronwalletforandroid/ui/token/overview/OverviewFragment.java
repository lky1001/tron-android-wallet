package com.devband.tronwalletforandroid.ui.token.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronlib.dto.Token;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonFragment;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.ui.token.TokenDetailActivity;
import com.thefinestartist.finestwebview.FinestWebView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewFragment extends CommonFragment implements OverviewView {

    @Inject
    OverviewPresenter mOverviewPresenter;
    
    @BindView(R.id.token_name_text)
    TextView mTokenNameText;

    @BindView(R.id.visit_website_button)
    ImageView mVisitWebsiteButton;

    @BindView(R.id.token_desc_text)
    TextView mTokenDescText;

    @BindView(R.id.token_website_text)
    TextView mTokenWebsiteText;

    @BindView(R.id.token_total_supply_text)
    TextView mTokenTotalSupplyText;

    @BindView(R.id.token_issuer_text)
    TextView mTokenIssuerText;

    @BindView(R.id.token_start_date_text)
    TextView mTokenStartDateText;

    @BindView(R.id.token_end_date_text)
    TextView mTokenEndDateText;

    @BindView(R.id.token_holders_text)
    TextView mTokenHoldersText;

    @BindView(R.id.token_total_transactions_text)
    TextView mTokenTotalTransactionsText;

    private String mTokenName;

    public static Fragment newInstance(@NonNull String tokenName) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle(1);
        args.putString(TokenDetailActivity.EXTRA_TOKEN_NAME, tokenName);

        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_overview, container, false);
        ButterKnife.bind(this, view);

        mTokenName = getArguments().getString(TokenDetailActivity.EXTRA_TOKEN_NAME);

        mOverviewPresenter.onCreate();

        mOverviewPresenter.loadTokenInfo(mTokenName);
        
        return view;
    }

    @Override
    protected void refresh() {
        if (isAdded()) {
            mOverviewPresenter.loadTokenInfo(mTokenName);
        }
    }

    @Override
    public void tokenInfoLoadSuccess(@NonNull Token token) {
        if (!isAdded()) {
            return;
        }
        mTokenNameText.setText(token.getName());
        mTokenDescText.setText(token.getDescription());
        mTokenWebsiteText.setText(token.getUrl());
        mTokenTotalSupplyText.setText(Constants.numberFormat.format(token.getTotalSupply()));
        Utils.setAccountDetailAction(getContext(), mTokenIssuerText, token.getOwnerAddress());
        mTokenHoldersText.setText(Constants.numberFormat.format(token.getNrOfTokenHolders()));
        mTokenTotalTransactionsText.setText(Constants.numberFormat.format(token.getTotalTransactions()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z", Locale.US);

        mTokenStartDateText.setText(sdf.format(new Date(token.getStartTime())));
        mTokenEndDateText.setText(sdf.format(new Date(token.getEndTime())));

        mVisitWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.visit_external_site_text)
                        .content(token.getUrl() + " " + getString(R.string.external_website_warning_msg))
                        .titleColorRes(android.R.color.black)
                        .contentColorRes(android.R.color.black)
                        .backgroundColorRes(android.R.color.white)
                        .positiveText(R.string.visit_site_text)
                        .negativeText(R.string.cancel_text)
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            new FinestWebView.Builder(getActivity()).show(token.getUrl());
                        }).show();
            }
        });

        hideDialog();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        if (isAdded()) {
            hideDialog();
            Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }
}