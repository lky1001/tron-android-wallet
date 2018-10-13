package com.devband.tronwalletforandroid.ui.importkey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.tron.WalletAppManager;
import com.devband.tronwalletforandroid.ui.main.MainActivity;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ImportPrivateKeyActivity extends CommonActivity implements ImportPrivateKeyView {

    @Inject
    ImportPrivateKeyPresenter mImportPrivateKeyPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_private_key)
    EditText mInputPrivateKey;

    @BindView(R.id.input_password)
    EditText mInputPassword;

    @BindView(R.id.btn_create_wallet)
    Button mCreateWalletButton;

    @BindView(R.id.agree_lost_password)
    CheckBox mChkLostPassword;

    @BindView(R.id.agree_lost_password_recover)
    CheckBox mChkLostPasswordRecover;

    private boolean mCheckPrivateKey;

    private boolean mCheckPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_private_key);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_create_wallet);
        }

        mImportPrivateKeyPresenter.onCreate();

        addDisposable(RxTextView.textChanges(mInputPrivateKey)
                .debounce(1, TimeUnit.SECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pk -> {
                    mCheckPrivateKey = !TextUtils.isEmpty(pk);

                    checkInputRequired();
                }));

        addDisposable(RxTextView.textChanges(mInputPassword)
                .debounce(1, TimeUnit.SECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(password -> {
                    mCheckPassword = WalletAppManager.passwordValid(password);

                    checkInputRequired();
                }));
    }

    private void checkInputRequired() {
        if (mCheckPassword && mCheckPrivateKey) {
            mCreateWalletButton.setEnabled(true);
        } else {
            mCreateWalletButton.setEnabled(false);
        }
    }

    @OnClick(R.id.btn_create_wallet)
    public void onCreateAccountClick() {
        if (!mChkLostPassword.isChecked() || !mChkLostPasswordRecover.isChecked()) {
            Toast.makeText(ImportPrivateKeyActivity.this, getString(R.string.need_all_agree),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog(null, getString(R.string.loading_msg));
        mImportPrivateKeyPresenter.createWallet(mInputPrivateKey.getText().toString(), mInputPassword.getText().toString());
    }

    @Override
    public void createdWallet() {
        hideDialog();
        startActivity(MainActivity.class);
        finishActivity();
    }

    @Override
    public void passwordError() {
        hideDialog();
        Toast.makeText(ImportPrivateKeyActivity.this, getString(R.string.password_error),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerWalletError() {
        hideDialog();
        Toast.makeText(ImportPrivateKeyActivity.this, getString(R.string.register_wallet_error),
                Toast.LENGTH_SHORT).show();
    }
}
