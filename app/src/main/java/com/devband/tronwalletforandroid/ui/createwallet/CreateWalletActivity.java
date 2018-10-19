package com.devband.tronwalletforandroid.ui.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.backupaccount.BackupAccountActivity;
import com.devband.tronwalletforandroid.ui.importkey.ImportPrivateKeyActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CreateWalletActivity extends CommonActivity implements CreateWalletView {

    @Inject
    CreateWalletPresenter mCreateWalletPresenter;

    @Inject
    CustomPreference mCustomPreference;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_password)
    EditText mInputPassword;

    @BindView(R.id.btn_create_wallet)
    Button mCreateWalletButton;

    @BindView(R.id.btn_import_private_key)
    Button mImportPrivateKeyButton;

    @BindView(R.id.agree_lost_password)
    CheckBox mChkLostPassword;

    @BindView(R.id.agree_lost_password_recover)
    CheckBox mChkLostPasswordRecover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_create_wallet);
        }

        mCustomPreference.setMigrationDb(true);

        mCreateWalletPresenter.onCreate();

        addDisposable(RxTextView.textChanges(mInputPassword)
                .debounce(1, TimeUnit.SECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(password -> {
                    if (password.length() >= WalletAppManager.MIN_PASSWORD_LENGTH) {
                        mCreateWalletButton.setEnabled(true);
                    } else {
                        mCreateWalletButton.setEnabled(false);
                    }
                }));

        addDisposable(RxView.clicks(mImportPrivateKeyButton)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(view -> {
                    startActivity(ImportPrivateKeyActivity.class);
                    finishActivity();
                }));
    }

    @OnClick(R.id.btn_create_wallet)
    public void onCreateWalletClick() {
        if (!mChkLostPassword.isChecked()
                || !mChkLostPasswordRecover.isChecked()) {
            Toast.makeText(CreateWalletActivity.this, getString(R.string.need_all_agree),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(null, getString(R.string.loading_msg));
        mCreateWalletPresenter.createWallet(mInputPassword.getText().toString());
    }

    @Override
    public void createdWallet(@NonNull byte[] aesKey) {
        hideDialog();
        Intent intent = new Intent(CreateWalletActivity.this, BackupAccountActivity.class);
        intent.putExtra(BackupAccountActivity.EXTRA_AES_KEY, aesKey);
        startActivity(intent);
        finishActivity();
    }

    @Override
    public void passwordError() {
        hideDialog();
        Toast.makeText(CreateWalletActivity.this, getString(R.string.password_error),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerWalletError() {
        hideDialog();
        Toast.makeText(CreateWalletActivity.this, getString(R.string.register_wallet_error),
                Toast.LENGTH_SHORT).show();
    }
}
