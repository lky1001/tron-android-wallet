package com.devband.tronwalletforandroid.ui.myaccount;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.tron.AccountManager;

import org.tron.protos.Protocol;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccountActivity extends CommonActivity implements MyAccountView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.account_spinner)
    Spinner mAccountSpinner;

    @BindView(R.id.address_text)
    TextView mAddressText;

    @BindView(R.id.freeze_button)
    Button mFreezeButton;

    @BindView(R.id.unfreeze_button)
    Button mUnFreezeButton;

    @BindView(R.id.frozen_trx_balance_text)
    TextView mFrozenTrxBalanceText;

    @BindView(R.id.frozen_trx_expired_text)
    TextView mFrozenTrxExpiredText;

    @BindView(R.id.tokens_layout)
    LinearLayout mTokensLayout;

    private DecimalFormat df = new DecimalFormat("#,##0");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ArrayAdapter<AccountModel> mAccountAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_my_account);
        }

        mPresenter = new MyAccountPresenter(this);
        mPresenter.onCreate();

        initAccountList();
    }

    private void initAccountList() {
        List<AccountModel> accountModelList = ((MyAccountPresenter) mPresenter).getAccountList();

        mAccountAdapter = new ArrayAdapter<>(MyAccountActivity.this, android.R.layout.simple_spinner_item,
                accountModelList);

        mAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAccountSpinner.setAdapter(mAccountAdapter);
        mAccountSpinner.setOnItemSelectedListener(mAccountItemSelectedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void displayAccountInfo(Protocol.Account account) {
        mAddressText.setText(AccountManager.encode58Check(account.getAddress().toByteArray()));

        for (String key : account.getAssetMap().keySet()) {
            View v = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.list_item_token, null);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));

            TextView tokenNameText = v.findViewById(R.id.token_name_text);
            TextView tokenAmountText = v.findViewById(R.id.token_amount_text);

            tokenNameText.setText(key);
            tokenAmountText.setText(df.format(account.getAssetMap().get(key)));
            mTokensLayout.addView(v);
        }

        mFreezeButton.setVisibility(View.VISIBLE);

        long frozenBalance = 0;
        long expiredTime = 0;

        if (account.getFrozenCount() > 0) {
            for (Protocol.Account.Frozen frozen : account.getFrozenList()) {
                frozenBalance += frozen.getFrozenBalance();
                if (frozen.getExpireTime() > expiredTime) {
                    expiredTime = frozen.getExpireTime();
                }
            }

            mUnFreezeButton.setVisibility(View.VISIBLE);
        } else {
            mUnFreezeButton.setVisibility(View.GONE);
        }

        mFrozenTrxBalanceText.setText(df.format(frozenBalance));
        if (expiredTime > 0) {
            mFrozenTrxExpiredText.setText(sdf.format(new Date(expiredTime)));
        } else {
            mFrozenTrxExpiredText.setText("-");
        }
    }

    @OnClick(R.id.freeze_button)
    public void onFreezeClick() {

    }

    @OnClick(R.id.unfreeze_button)
    public void onUnFreezeClick() {

    }

    private android.widget.AdapterView.OnItemSelectedListener mAccountItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int pos, long id) {
            AccountModel accountModel = mAccountAdapter.getItem(pos);
            //((MainPresenter) mPresenter).changeLoginAccount(accountModel);
            //checkLoginState();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };
}
