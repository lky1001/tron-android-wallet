package com.devband.tronwalletforandroid.ui.importwallet;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.tron.Tron;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportWalletActivity extends CommonActivity implements ImportWalletView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_password)
    EditText mInputPassword;

    @BindView(R.id.input_private_key)
    EditText mInputPrivateKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_import_wallet);
        }

        mPresenter = new ImportWalletPresenter(this);
        mPresenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStoragePermission();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_import_wallet)
    public void onImportWalletClick() {
        String password = mInputPassword.getText().toString();

        if (password.length() < Tron.MIN_PASSWORD_LENGTH) {
            Toast.makeText(ImportWalletActivity.this, getString(R.string.invalid_password),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String privateKey = mInputPrivateKey.getText().toString();

        if (privateKey.length() != Tron.PRIVATE_KEY_SIZE) {
            Toast.makeText(ImportWalletActivity.this, getString(R.string.invalid_private_key),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // todo - warning msg. remove exist wallet.

        boolean result = ((ImportWalletPresenter) mPresenter).importWallet(password, privateKey);

        if (result) {
            Toast.makeText(ImportWalletActivity.this, getString(R.string.import_wallet_success),
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(ImportWalletActivity.this, getString(R.string.invalid_password_or_private_key),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
