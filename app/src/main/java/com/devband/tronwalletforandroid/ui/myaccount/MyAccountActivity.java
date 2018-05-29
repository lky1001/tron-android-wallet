package com.devband.tronwalletforandroid.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.database.model.AccountModel;
import com.devband.tronwalletforandroid.ui.address.AddressActivity;
import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.main.dto.Frozen;
import com.devband.tronwalletforandroid.ui.main.dto.TronAccount;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyAccountActivity extends CommonActivity implements MyAccountView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.account_spinner)
    Spinner mAccountSpinner;

    @BindView(R.id.name_text)
    TextView mNameText;

    @BindView(R.id.address_text)
    TextView mAddressText;

    @BindView(R.id.balance_text)
    TextView mBalanceText;

    @BindView(R.id.tron_power_text)
    TextView mTronPowerText;

    @BindView(R.id.entropy_text)
    TextView mEntropyText;

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

    private ArrayAdapter<AccountModel> mAccountAdapter;

    private AccountModel mSelectedAccount;
    private long mAccountBalance;

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
        ((MyAccountPresenter) mPresenter).getAccountList()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<List<AccountModel>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<AccountModel> accountModelList) {
                mAccountAdapter = new ArrayAdapter<>(MyAccountActivity.this, android.R.layout.simple_spinner_item,
                        accountModelList);

                mAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mAccountSpinner.setAdapter(mAccountAdapter);

                for (int i = 0; i < accountModelList.size(); i++) {
                    int id = ((MyAccountPresenter) mPresenter).getLoginAccountIndex();
                    if (id == accountModelList.get(i).getId()) {
                        mAccountSpinner.setSelection(i);
                        break;
                    }
                }

                mAccountSpinner.setOnItemSelectedListener(mAccountItemSelectedListener);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void displayAccountInfo(@NonNull String address, @NonNull TronAccount account) {
        Log.d("", address);
        mAccountBalance = (long) (account.getBalance() / Constants.ONE_TRX);

        if (TextUtils.isEmpty(account.getName())) {
            mNameText.setText("-");
        } else {
            mNameText.setText(account.getName());
        }

        mAddressText.setText(address);
        mBalanceText.setText(Constants.tronBalanceFormat.format(mAccountBalance) + " " + Constants.TRON_SYMBOL);
        mEntropyText.setText(Constants.tronBalanceFormat.format(account.getBandwidth()));
        mTokensLayout.removeAllViews();

        if (!account.getAssetList().isEmpty()) {
            for (Asset asset : account.getAssetList()) {
                View v = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.list_item_my_token, null);
                v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT));

                TextView tokenNameText = v.findViewById(R.id.token_name_text);
                TextView tokenAmountText = v.findViewById(R.id.token_amount_text);

                tokenNameText.setText(asset.getName());
                tokenAmountText.setText(Constants.tronBalanceFormat.format(asset.getBalance()));
                mTokensLayout.addView(v);
            }
        } else {
            View v = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.list_item_my_token, null);
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

        if (!account.getFrozenList().isEmpty()) {
            for (Frozen frozen : account.getFrozenList()) {
                frozenBalance += frozen.getFrozenBalance();
                if (frozen.getExpireTime() > expiredTime) {
                    expiredTime = frozen.getExpireTime();
                }
            }

            mUnFreezeButton.setVisibility(View.VISIBLE);
        } else {
            mUnFreezeButton.setVisibility(View.GONE);
        }

        mTronPowerText.setText(Constants.tronBalanceFormat.format(frozenBalance / Constants.ONE_TRX) + " " + Constants.TRON_SYMBOL);
        mFrozenTrxBalanceText.setText(Constants.tronBalanceFormat.format(frozenBalance / Constants.ONE_TRX) + " " + Constants.TRON_SYMBOL);
        if (expiredTime > 0) {
            mFrozenTrxExpiredText.setText(Constants.sdf.format(new Date(expiredTime)));
        } else {
            mFrozenTrxExpiredText.setText("-");
        }

        hideDialog();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void hideDialog() {
        super.hideDialog();
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(MyAccountActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successFreezeBalance() {
        ((MyAccountPresenter) mPresenter).getAccountAccountInfo();
    }

    @Override
    public void unableToUnfreeze() {
        hideDialog();
        Toast.makeText(MyAccountActivity.this, getString(R.string.unable_to_unfreeze_msg), Toast.LENGTH_SHORT).show();
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

                            Log.d("", privateKey);
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
        if (mAccountBalance == 0) {
            Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_freeze_amount),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_freeze_trx)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .customView(R.layout.dialog_freeze_trx, false);

        MaterialDialog dialog = builder.build();

        Button freezeButton = (Button) dialog.getCustomView().findViewById(R.id.btn_freeze);
        CheckBox agreeFreezeCheckBox = (CheckBox) dialog.getCustomView().findViewById(R.id.agree_freeze_balance);
        EditText inputAmount = (EditText) dialog.getCustomView().findViewById(R.id.input_amount);
        EditText inputPassword = (EditText) dialog.getCustomView().findViewById(R.id.input_password);

        freezeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check freeze balance
                long freezeBalance = 0;
                try {
                    freezeBalance = Long.parseLong(inputAmount.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_amount),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (freezeBalance > mAccountBalance) {
                    Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_amount),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = inputPassword.getText().toString();
                if (TextUtils.isEmpty(password) || !((MyAccountPresenter) mPresenter).matchPassword(password)) {
                    Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_password),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean agree = agreeFreezeCheckBox.isChecked();

                if (!agree) {
                    Toast.makeText(MyAccountActivity.this, getString(R.string.need_all_agree),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                ((MyAccountPresenter) mPresenter).freezeBalance((long) (freezeBalance * Constants.ONE_TRX));
            }
        });

        dialog.show();
    }

    @OnClick(R.id.unfreeze_button)
    public void onUnFreezeClick() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_unfreeze_trx)
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
                            ((MyAccountPresenter) mPresenter).unfreezeBalance();
                        } else {
                            Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_password),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @OnClick(R.id.tron_power_layout)
    public void onTronPowerHelpClick() {
        new MaterialDialog.Builder(MyAccountActivity.this)
                .title(getString(R.string.tron_power_text))
                .content(getString(R.string.tron_power_help_text))
                .titleColorRes(android.R.color.black)
                .contentColorRes(android.R.color.black)
                .backgroundColorRes(android.R.color.white)
                .autoDismiss(true)
                .build()
                .show();
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
