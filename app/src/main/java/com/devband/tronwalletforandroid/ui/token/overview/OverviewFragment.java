package com.devband.tronwalletforandroid.ui.token.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewFragment extends BaseFragment {

    public static BaseFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_overview, container, false);
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