package com.devband.tronwalletforandroid.ui.login;

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

public class LoginActivity extends CommonActivity implements LoginView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_password)
    EditText mInputPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_login);
        }

        mPresenter = new LoginPresenter(this);
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
                finishActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        String password = mInputPassword.getText().toString();

        ((LoginPresenter) mPresenter).loginWallet(password);
    }

    @Override
    public void loginResult(int result) {
        if (result == Tron.ERROR_INVALID_PASSWORD) {
            Toast.makeText(LoginActivity.this, getString(R.string.invalid_password),
                    Toast.LENGTH_SHORT).show();
        } else if (result == Tron.ERROR_WALLET_DOES_NOT_EXIST) {
            Toast.makeText(LoginActivity.this, getString(R.string.wallet_does_not_exist),
                    Toast.LENGTH_SHORT).show();
        } else if (result == Tron.SUCCESS) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_success),
                    Toast.LENGTH_SHORT).show();

            finishActivity();
        }
    }
}
