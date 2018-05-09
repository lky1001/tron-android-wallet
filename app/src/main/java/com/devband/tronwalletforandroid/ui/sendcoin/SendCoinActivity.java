package com.devband.tronwalletforandroid.ui.sendcoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.qrscan.QrScanActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendCoinActivity extends CommonActivity implements SendCoinView {

    public static final int QR_SCAN_ADDRESS = 3321;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_address)
    public EditText mInputAddress;

    @BindView(R.id.input_amount)
    public EditText mInputAmount;

    @BindView(R.id.input_password)
    public EditText mInputPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_coin_activity);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mInputAddress.setText("27diGPR8i8sv168sGsZm2FFVUm3bVELuZN4");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_send_coin);
        }

        mPresenter = new SendCoinPresenter(this);
        mPresenter.onCreate();
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

    @OnClick(R.id.btn_send_trx)
    public void onSendTrxClick() {
        String address = mInputAddress.getText().toString();

        if (address.isEmpty()) {
            Toast.makeText(SendCoinActivity.this, getString(R.string.invalid_address),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String amountText = mInputAmount.getText().toString();

        double amountDouble = 0;

        try {
            amountDouble = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(SendCoinActivity.this, getString(R.string.invalid_amount),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountDouble <= 0) {
            Toast.makeText(SendCoinActivity.this, getString(R.string.invalid_amount),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String password = mInputPassword.getText().toString();

        long amount = (long) (amountDouble * Constants.REAL_TRX_AMOUNT);

        ((SendCoinPresenter) mPresenter).sendCoin(password, address, amount);
    }

    @Override
    public void sendCoinResult(boolean result) {
        if (result) {
            Toast.makeText(SendCoinActivity.this, getString(R.string.sending_coin_success),
                    Toast.LENGTH_SHORT).show();
            finishActivity();
        } else {
            Toast.makeText(SendCoinActivity.this, getString(R.string.sending_coin_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_qrcode_scan)
    public void onQrcodeScanClick() {
        Intent qrScanIntent = new Intent(SendCoinActivity.this, QrScanActivity.class);
        startActivityForResult(qrScanIntent, QR_SCAN_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QR_SCAN_ADDRESS) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra(QrScanActivity.EXTRA_QR_CODE_RESULT);
                mInputAddress.setText(result);
            }
        }
    }
}
