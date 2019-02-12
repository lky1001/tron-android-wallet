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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyAccountActivity extends CommonActivity implements MyAccountView {

    @Inject
    MyAccountPresenter mMyAccountPresenter;

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

    @BindView(R.id.bandwidth_text)
    TextView mBandwidthText;

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

        mMyAccountPresenter.onCreate();

        initAccountList();
    }

    private void initAccountList() {
        mMyAccountPresenter.getAccountList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accountModelList -> {
                    mAccountAdapter = new ArrayAdapter<>(MyAccountActivity.this, android.R.layout.simple_spinner_item,
                            accountModelList);

                    mAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mAccountSpinner.setAdapter(mAccountAdapter);

                    for (int i = 0; i < accountModelList.size(); i++) {
                        long id = mMyAccountPresenter.getLoginAccountIndex();
                        if (id == accountModelList.get(i).getId()) {
                            mAccountSpinner.setSelection(i);
                            break;
                        }
                    }

                    mAccountSpinner.setOnItemSelectedListener(mAccountItemSelectedListener);
                }, e -> {});
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyAccountPresenter.onResume();
    }

    @Override
    public void displayAccountInfo(@NonNull String address, @Nullable TronAccount account) {
        if (isFinishing()) {
            return;
        }

        mAddressText.setText(address);

        if (account != null) {
            mAccountBalance = (long) (account.getBalance() / Constants.ONE_TRX);

            if (TextUtils.isEmpty(account.getName())) {
                mNameText.setText("-");
            } else {
                mNameText.setText(account.getName());
            }

            mBalanceText.setText(Constants.tokenBalanceFormat.format(mAccountBalance) + " " + Constants.TRON_SYMBOL);
            mBandwidthText.setText(account.getBandwidth() == 0 ? "-" : Constants.tokenBalanceFormat.format(account.getBandwidth()));
            mTokensLayout.removeAllViews();

            if (!account.getAssetList().isEmpty()) {
                for (Asset asset : account.getAssetList()) {
                    View v = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.list_item_my_token, null);
                    v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT));

                    TextView tokenNameText = v.findViewById(R.id.token_name_text);
                    TextView tokenAmountText = v.findViewById(R.id.token_amount_text);
                    ImageButton favoriteButton = v.findViewById(R.id.token_favorite_button);
                    favoriteButton.setVisibility(View.VISIBLE);
                    favoriteButton.setTag(asset);

                    if (mMyAccountPresenter.isFavoriteToken(asset.getName())) {
                        favoriteButton.setImageResource(R.drawable.ic_star);
                    } else {
                        favoriteButton.setImageResource(R.drawable.ic_star_outline);
                    }

                    favoriteButton.setOnClickListener(view -> {
                        if (view.getTag() instanceof Asset) {
                            Asset tag = (Asset) view.getTag();

                            if (mMyAccountPresenter.isFavoriteToken(tag.getName())) {
                                mMyAccountPresenter.removeFavorite(tag.getName());
                                favoriteButton.setImageResource(R.drawable.ic_star_outline);
                            } else {
                                mMyAccountPresenter.doFavorite(tag.getName());
                                favoriteButton.setImageResource(R.drawable.ic_star);
                            }
                        }
                    });

                    tokenNameText.setText(asset.getDisplayName());
                    tokenAmountText.setText(Constants.tokenBalanceFormat.format(asset.getBalance()));
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

            mTronPowerText.setText(Constants.tokenBalanceFormat.format(frozenBalance / Constants.ONE_TRX) + " " + Constants.TRON_SYMBOL);
            mFrozenTrxBalanceText.setText(Constants.tokenBalanceFormat.format(frozenBalance / Constants.ONE_TRX) + " " + Constants.TRON_SYMBOL);
            if (expiredTime > 0) {
                mFrozenTrxExpiredText.setText(Constants.sdf.format(new Date(expiredTime)));
            } else {
                mFrozenTrxExpiredText.setText("-");
            }
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
        mMyAccountPresenter.getAccountAccountInfo();
    }

    @Override
    public void unableToUnfreeze() {
        hideDialog();
        Toast.makeText(MyAccountActivity.this, getString(R.string.unable_to_unfreeze_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInvalidPasswordMsg() {
        hideDialog();
        Toast.makeText(MyAccountActivity.this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successDelete() {
        hideDialog();

        initAccountList();
    }

    @OnClick(R.id.btn_remove_account)
    public void onRemoveAccountClick() {
        if (mMyAccountPresenter.getAccountCount() < 2) {
            Toast.makeText(MyAccountActivity.this, getString(R.string.remove_account_error_msg),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_remove_acocunt)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .customView(R.layout.dialog_remove_account, false);

        MaterialDialog dialog = builder.build();

        Button removeButton = (Button) dialog.getCustomView().findViewById(R.id.btn_remove_account);
        TextView accountNameText = (TextView) dialog.getCustomView().findViewById(R.id.account_name_text);
        EditText accountNameInput = (EditText) dialog.getCustomView().findViewById(R.id.input_account_name);

        removeButton.setEnabled(false);

        final AccountModel loginAccount = mMyAccountPresenter.getLoginAccount();

        addDisposable(RxTextView.textChanges(accountNameInput)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(accountName -> {
                    if (loginAccount.getName().equals(accountName)) {
                        removeButton.setBackgroundResource(R.color.colorAccent);
                        removeButton.setEnabled(true);
                    } else {
                        removeButton.setBackgroundResource(R.color.copy_address_button_color);
                        removeButton.setEnabled(false);
                    }
                }));

        accountNameText.setText(loginAccount.getName());

        removeButton.setOnClickListener(v -> {
            if (loginAccount.getName().equals(accountNameInput.getText().toString())) {
                mMyAccountPresenter.removeAccount(loginAccount.getId(), accountNameInput.getText().toString());
            }
        });

        dialog.show();
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

                        if (!TextUtils.isEmpty(password) && mMyAccountPresenter.matchPassword(password)) {
                            String privateKey = mMyAccountPresenter.getLoginPrivateKey(password);

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

        Button maxButton = (Button) dialog.getCustomView().findViewById(R.id.max_button);
        Button freezeButton = (Button) dialog.getCustomView().findViewById(R.id.btn_freeze);
        CheckBox agreeFreezeCheckBox = (CheckBox) dialog.getCustomView().findViewById(R.id.agree_freeze_balance);
        EditText inputAmount = (EditText) dialog.getCustomView().findViewById(R.id.input_amount);
        EditText inputPassword = (EditText) dialog.getCustomView().findViewById(R.id.input_password);

        maxButton.setOnClickListener(view -> {
            inputAmount.setText(String.valueOf(mAccountBalance));
        });

        freezeButton.setOnClickListener(view -> {
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
            if (TextUtils.isEmpty(password) || !mMyAccountPresenter.matchPassword(password)) {
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
            mMyAccountPresenter.freezeBalance(password, (long) (freezeBalance * Constants.ONE_TRX));
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

                        if (!TextUtils.isEmpty(password) && mMyAccountPresenter.matchPassword(password)) {
                            mMyAccountPresenter.unfreezeBalance(password);
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
            mMyAccountPresenter.changeLoginAccount(accountModel);
            mMyAccountPresenter.getAccountAccountInfo();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };
}
