package com.devband.tronwalletforandroid.ui.sendcoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.main.MainActivity;
import com.devband.tronwalletforandroid.ui.qrscan.QrScanActivity;

import org.tron.protos.Protocol;

import java.text.DecimalFormat;

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

    @BindView(R.id.token_spinner)
    public Spinner mTokenSpinner;

    @BindView(R.id.btn_send_trx)
    public Button mSendTrxBtn;

    @BindView(R.id.btn_qrcode_scan)
    public ImageButton mQrCodeScanBtn;

    private ArrayAdapter<String> mTokenAdapter;

    private double mAvailableAmount;

    private boolean mFromDonations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_coin_activity);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_send_coin);
        }

        mFromDonations = intent.getBooleanExtra(MainActivity.EXTRA_FROM_DONATIONS, false);

        if (intent.getBooleanExtra(QrScanActivity.EXTRA_FROM_TRON_PAY_MENU, false)) {
            String result = intent.getStringExtra(QrScanActivity.EXTRA_QR_CODE_ADDRESS);
            String amount = intent.getStringExtra(QrScanActivity.EXTRA_QR_CODE_AMOUNT);
            mInputAddress.setText(result);
            mInputAmount.setText(amount);
        } else if (mFromDonations) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.title_donations);
            }

            // todo - developer tron address
            //mInputAddress.setText(result);
            mInputAddress.setEnabled(false);
            mSendTrxBtn.setText(R.string.title_donations);
            mQrCodeScanBtn.setVisibility(View.GONE);
        }

        mPresenter = new SendCoinPresenter(this);
        mPresenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressDialog(null, getString(R.string.loading_msg));
        mPresenter.onResume();
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

        if (amountDouble <= 0 || amountDouble > mAvailableAmount) {
            Toast.makeText(SendCoinActivity.this, getString(R.string.invalid_amount),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String password = mInputPassword.getText().toString();

        long amount = (long) (amountDouble * Constants.REAL_TRX_AMOUNT);

        showProgressDialog(null, getString(R.string.loading_msg));
        ((SendCoinPresenter) mPresenter).sendCoin(password, address, amount);
    }

    @Override
    public void sendCoinResult(boolean result) {
        hideDialog();

        if (result) {
            if (mFromDonations) {
                // todo - thanks donations message
            } else {
                Toast.makeText(SendCoinActivity.this, getString(R.string.sending_coin_success),
                        Toast.LENGTH_SHORT).show();
                finishActivity();
            }
        } else {
            Toast.makeText(SendCoinActivity.this, getString(R.string.sending_coin_failed),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void invalidPassword() {
        hideDialog();

        Toast.makeText(SendCoinActivity.this, getString(R.string.invalid_password),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayAccountInfo(Protocol.Account account) {
        DecimalFormat df = new DecimalFormat("#,##0.00000000");

        mAvailableAmount = ((double) account.getBalance()) / Constants.REAL_TRX_AMOUNT;

        String tronAmount = "TRX (" + df.format(mAvailableAmount) + ")";

        mTokenAdapter = new ArrayAdapter<>(SendCoinActivity.this, android.R.layout.simple_spinner_item,
                new String[] {
                        tronAmount
                });

        mTokenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTokenSpinner.setAdapter(mTokenAdapter);
        mTokenSpinner.setOnItemSelectedListener(mTokenItemSelectedListener);

        hideDialog();
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
                String result = data.getStringExtra(QrScanActivity.EXTRA_QR_CODE_ADDRESS);
                String amount = data.getStringExtra(QrScanActivity.EXTRA_QR_CODE_AMOUNT);
                mInputAddress.setText(result);
                mInputAmount.setText(amount);
            }
        }
    }

    private android.widget.AdapterView.OnItemSelectedListener mTokenItemSelectedListener = new android.widget.AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(android.widget.AdapterView<?> adapterView, View view, int pos, long id) {

        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };
}
