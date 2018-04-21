package com.devband.tronwalletforandroid.ui.createaccount;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountActivity extends CommonActivity implements CreateAccountView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_address)
    EditText mInputAddress;

    @BindView(R.id.input_private_key)
    EditText mInputPrivateKey;

    @BindView(R.id.input_password)
    EditText mInputPassword;

    @BindView(R.id.btn_create_account)
    Button mBtnCreateAddress;

    @BindView(R.id.btn_copy_address)
    Button mBtnCopyAddress;

    @BindView(R.id.agree_lost_password)
    CheckBox mChkLostPassword;

    @BindView(R.id.agree_lost_password_recover)
    CheckBox mChkLostPasswordRecover;

    @BindView(R.id.agree_copy_account)
    CheckBox mChkCopyAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mPresenter = new CreateAccountPresenter(this);
        mPresenter.onCreate();

        mInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((CreateAccountPresenter) mPresenter).changedPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStoragePermission();
    }

    @Override
    public void displayAccountInfo(String privKey, String address) {
        Log.d(CreateAccountActivity.class.getSimpleName(), "privKey:" + privKey);
        Log.d(CreateAccountActivity.class.getSimpleName(), "address:" + address);

        if (privKey == null || privKey.isEmpty()) {
            mBtnCopyAddress.setEnabled(false);
            mBtnCreateAddress.setEnabled(false);
            return;
        } else {
            mBtnCopyAddress.setEnabled(true);
            mBtnCreateAddress.setEnabled(true);
        }

        mInputPrivateKey.setText(privKey);
        mInputAddress.setText(address);
    }

    @OnClick(R.id.btn_copy_address)
    public void onCopyClick() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("Private Key : ")
                .append(mInputPrivateKey.getText().toString())
                .append("\n")
                .append("Address : ")
                .append(mInputAddress.getText().toString());

        ClipData clip = ClipData.newPlainText("", sb.toString());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(CreateAccountActivity.this, getString(R.string.copy_address_msg),
                Toast.LENGTH_SHORT)
                .show();
    }

    @OnClick(R.id.btn_create_account)
    public void onCreateAccountClick() {
        if (!mChkLostPassword.isChecked()
                || !mChkLostPasswordRecover.isChecked()
                || !mChkCopyAccount.isChecked()) {
            Toast.makeText(CreateAccountActivity.this, getString(R.string.need_all_agree),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        finish();
    }
}
