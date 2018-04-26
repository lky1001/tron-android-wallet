package com.devband.tronwalletforandroid.ui.backupwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackupWalletActivity extends CommonActivity implements BackupWalletView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_address)
    EditText mInputAddress;

    @BindView(R.id.input_private_key)
    EditText mInputPrivateKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_wallet);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_backup_wallet);
        }

        mPresenter = new BackupWalletPresenter(this);
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
        new MaterialDialog.Builder(BackupWalletActivity.this)
                .title(R.string.backup_title)
                .content(R.string.backup_msg)
                .positiveText(R.string.next_text)
                .negativeText(R.string.cancen_text)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    startActivity(MainActivity.class);
                    finishActivity();
                }).show();
    }

    @Override
    public void displayWalletInfo(@NonNull String address, @NonNull String privateKey) {
        Log.d(BackupWalletActivity.class.getSimpleName(), "address : " + address);
        Log.d(BackupWalletActivity.class.getSimpleName(), "privateKey : " + privateKey);
        mInputAddress.setText(address);
        mInputPrivateKey.setText(privateKey);
    }
}
