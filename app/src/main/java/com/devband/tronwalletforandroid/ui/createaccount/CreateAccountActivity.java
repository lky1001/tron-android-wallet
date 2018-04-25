package com.devband.tronwalletforandroid.ui.createaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountActivity extends CommonActivity implements CreateAccountView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_account_nickname)
    EditText mInputAccountNickname;

    @BindView(R.id.input_password)
    EditText mInputPassword;

    @BindView(R.id.input_address)
    EditText mInputAddress;

    @BindView(R.id.input_private_key)
    EditText mInputPrivateKey;

    @BindView(R.id.btn_create_account)
    Button mBtnCreateAccount;

    @BindView(R.id.btn_copy_account_info)
    Button mBtnCopyAccount;

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_create_account);
        }

        mPresenter = new CreateAccountPresenter(this);
        mPresenter.onCreate();

        mInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nickname = mInputAccountNickname.getText().toString();
                ((CreateAccountPresenter) mPresenter).changedPassword(nickname, s.toString());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayAccountInfo(String privKey, String address) {
        Log.d(CreateAccountActivity.class.getSimpleName(), "privKey:" + privKey);
        Log.d(CreateAccountActivity.class.getSimpleName(), "address:" + address);

        if (privKey == null || privKey.isEmpty()) {
            mBtnCopyAccount.setEnabled(false);
            mBtnCreateAccount.setEnabled(false);
            return;
        } else {
            mBtnCopyAccount.setEnabled(true);
            mBtnCreateAccount.setEnabled(true);
        }

        mInputPrivateKey.setText(privKey);
        mInputAddress.setText(address);
    }

    @Override
    public void showProgressDialog(@Nullable String title, @NonNull String msg) {
        super.showProgressDialog(title, msg);
    }

    @Override
    public void hideProgressDialog() {
        hideDialog();
    }

    @Override
    public void createdWallet() {
        startActivity(MainActivity.class);
        finishActivity();
    }

    @Override
    public void errorCreatedWallet() {
        Toast.makeText(CreateAccountActivity.this, getString(R.string.error_create_wallet_msg),
                Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_copy_account_info)
    public void onCopyAccountClick() {
//        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//        StringBuilder sb = new StringBuilder();
//        sb.append("Private Key : ")
//                .append(mInputPrivateKey.getText().toString())
//                .append("\n")
//                .append("Address : ")
//                .append(mInputAddress.getText().toString());
//
//        ClipData clip = ClipData.newPlainText("", sb.toString());
//        clipboard.setPrimaryClip(clip);
//
//        Toast.makeText(CreateAccountActivity.this, getString(R.string.copy_account_msg),
//                Toast.LENGTH_SHORT)
//                .show();

        StringBuilder sb = new StringBuilder();
        sb.append("Private Key : ")
                .append(mInputPrivateKey.getText().toString())
                .append("\n")
                .append("Address : ")
                .append(mInputAddress.getText().toString());

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.choice_share_account)));
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

        ((CreateAccountPresenter) mPresenter).storeWallet();
    }
}
