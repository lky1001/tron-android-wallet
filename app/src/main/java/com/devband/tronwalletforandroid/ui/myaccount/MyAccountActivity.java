package com.devband.tronwalletforandroid.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.ui.address.AddressActivity;

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

    @BindView(R.id.balance_text)
    TextView mBalanceText;

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

    private AccountModel mSelectedAccount;

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
    public void displayAccountInfo(@NonNull String address, @NonNull Protocol.Account account) {
        mAddressText.setText(address);
        mBalanceText.setText(account.getBalance() / Constants.REAL_TRX_AMOUNT + " " + Constants.TRON_SYMBOL);
        mTokensLayout.removeAllViews();

        if (account.getAssetCount() > 0) {
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
        } else {
            View v = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.list_item_token, null);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));

            TextView tokenNameText = v.findViewById(R.id.token_name_text);
            TextView tokenAmountText = v.findViewById(R.id.token_amount_text);

            tokenNameText.setText(getString(R.string.no_tokens));
            tokenNameText.setGravity(Gravity.CENTER);
            tokenAmountText.setVisibility(View.GONE);
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

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void hideDialog() {
        super.hideDialog();
    }

    @OnClick(R.id.btn_export_private_key)
    public void onExportPrivateKeyClick() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_export_private_key)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(getString(R.string.input_password_text), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        dialog.dismiss();
                        String password = input.toString();

                        if (!TextUtils.isEmpty(password) && ((MyAccountPresenter) mPresenter).matchPassword(password)) {
                            String privateKey = ((MyAccountPresenter) mPresenter).getLoginPrivateKey();

                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, privateKey);
                            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.choice_share_private_key)));
                        } else {
                            Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_password),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @OnClick(R.id.btn_copy_address)
    public void onCopyAddressClick() {
        startActivity(AddressActivity.class);
    }

    @OnClick(R.id.freeze_button)
    public void onFreezeClick() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_freeze_trx)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .customView(R.layout.dialog_freeze_trx, false)
                .positiveText(R.string.confirm_text)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .show();
    }

    @OnClick(R.id.unfreeze_button)
    public void onUnFreezeClick() {

    }

    private android.widget.AdapterView.OnItemSelectedListener mAccountItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int pos, long id) {
            AccountModel accountModel = mAccountAdapter.getItem(pos);
            ((MyAccountPresenter) mPresenter).changeLoginAccount(accountModel);
            ((MyAccountPresenter) mPresenter).getAccountAccountInfo();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };
}
