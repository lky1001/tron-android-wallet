package com.devband.tronwalletforandroid.ui.sendcoin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendCoinActivity extends CommonActivity implements SendCoinView {

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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_send_coin);
        }

        mPresenter = new SendCoinPresenter(this);
        mPresenter.onCreate();
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
        Toast.makeText(SendCoinActivity.this, "TBD",
                Toast.LENGTH_SHORT).show();
    }
}
