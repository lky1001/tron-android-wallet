package com.devband.tronwalletforandroid.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.WalletAppManager;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.main.MainActivity;

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
            getSupportActionBar().setTitle(R.string.title_login);
        }

        mPresenter = new LoginPresenter(this);
        mPresenter.onCreate();

        mInputPassword.setText("1234567890");
        onLoginClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkStoragePermission();
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        String password = mInputPassword.getText().toString();

        showProgressDialog("", getString(R.string.loading_msg));
        ((LoginPresenter) mPresenter).loginWallet(password);
    }

    @Override
    public void loginResult(int result) {
        hideDialog();

        if (result == WalletAppManager.ERROR) {
            Toast.makeText(LoginActivity.this, getString(R.string.invalid_password),
                    Toast.LENGTH_SHORT).show();
        } else if (result == Tron.SUCCESS) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_success),
                    Toast.LENGTH_SHORT).show();

            startActivity(MainActivity.class);
            finishActivity();
        }
    }
}
