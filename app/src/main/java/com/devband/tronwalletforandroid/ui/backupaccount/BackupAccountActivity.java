package com.devband.tronwalletforandroid.ui.backupaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.WalletAppManager;
import com.devband.tronwalletforandroid.ui.createwallet.CreateWalletActivity;
import com.devband.tronwalletforandroid.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackupAccountActivity extends CommonActivity implements BackupAccountView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_address)
    EditText mInputAddress;

    @BindView(R.id.input_private_key)
    EditText mInputPrivateKey;

    @BindView(R.id.agree_lost_private_key_recover)
    CheckBox mAgreeCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_wallet);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_backup_account);
        }

        mPresenter = new BackupAccountPresenter(this);
        mPresenter.onCreate();
    }

    @OnClick(R.id.btn_copy_wallet_info)
    public void onCopyAccountInfoClick() {
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

    @OnClick(R.id.btn_next)
    public void onNextClick() {
        if (!mAgreeCheckBox.isChecked()) {
            Toast.makeText(BackupAccountActivity.this, getString(R.string.need_all_agree),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        new MaterialDialog.Builder(BackupAccountActivity.this)
                .title(R.string.backup_title)
                .content(R.string.backup_msg)
                .positiveText(R.string.next_text)
                .negativeText(R.string.cancen_text)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    showProgressDialog(null, getString(R.string.loading_msg));
                    ((BackupAccountPresenter) mPresenter).agreeTerms(true);
                }).show();
    }

    @Override
    public void displayAccountInfo(@NonNull String address, @NonNull String privateKey) {
        Log.d(BackupAccountActivity.class.getSimpleName(), "address : " + address);
        Log.d(BackupAccountActivity.class.getSimpleName(), "privateKey : " + privateKey);
        mInputAddress.setText(address);
        mInputPrivateKey.setText(privateKey);
    }

    @Override
    public void startMainActivity() {
        startActivity(MainActivity.class);
        finishActivity();
    }
}
