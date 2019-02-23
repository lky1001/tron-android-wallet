package com.devband.tronwalletforandroid.ui.sendtrc10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.ui.main.dto.Asset;
import com.devband.tronwalletforandroid.ui.more.MoreActivity;
import com.devband.tronwalletforandroid.ui.qrscan.QrScanActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendTrc10Activity extends CommonActivity implements SendTrc10View {

    @Inject
    SendTrc10Presenter mSendTrc10Presenter;

    private static final int CAMERA_PERMISSION_REQ = 9929;
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

    private ArrayAdapter<Asset> mTokenAdapter;

    private Asset mSelectedAsset;

    private boolean mFromDonations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_trc10_token);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_send_trc10_token);
        }

        mFromDonations = intent.getBooleanExtra(MoreActivity.EXTRA_FROM_DONATIONS, false);

        if (intent.getBooleanExtra(QrScanActivity.EXTRA_FROM_TRON_PAY_MENU, false)) {
            String result = intent.getStringExtra(QrScanActivity.EXTRA_QR_CODE_ADDRESS);
            String amount = intent.getStringExtra(QrScanActivity.EXTRA_QR_CODE_AMOUNT);
            mInputAddress.setText(result);
            mInputAmount.setText(amount);
        } else if (mFromDonations) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.title_donations);
            }

            mInputAddress.setText(getString(R.string.donation_address_text));
            mInputAddress.setEnabled(false);
            mSendTrxBtn.setText(R.string.title_donations);
            mQrCodeScanBtn.setVisibility(View.GONE);
        }

        mSendTrc10Presenter.onCreate();
    }

    @Override
    protected void onResume() {
        showProgressDialog(null, getString(R.string.loading_msg));
        mSendTrc10Presenter.onResume();
        super.onResume();
    }

    @OnClick(R.id.max_button)
    public void onMaxBtnClick() {
        mInputAmount.setText(Utils.getTrxFormat(mSelectedAsset.getBalance()));
    }

    @OnClick(R.id.btn_send_trx)
    public void onSendTrxClick() {
        String address = mInputAddress.getText().toString();
        address = address.trim();

        if (address.isEmpty()) {
            Toast.makeText(SendTrc10Activity.this, getString(R.string.invalid_address),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String amountText = mInputAmount.getText().toString();
        String removeCommaAmountText = amountText.replace(",", "");

        double amountDouble = 0;

        try {
            amountDouble = Double.parseDouble(removeCommaAmountText);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(SendTrc10Activity.this, getString(R.string.invalid_amount),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountDouble <= 0 || amountDouble > mSelectedAsset.getBalance()) {
            Toast.makeText(SendTrc10Activity.this, getString(R.string.invalid_amount),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double finalAmountDouble = amountDouble;

        StringBuilder sb = new StringBuilder();

        sb.append(getString(R.string.send_token_warning_msg))
                .append("\n\n")
                .append(getString(R.string.send_token_address_text))
                .append(address)
                .append("\n")
                .append(getString(R.string.send_token_token_text))
                .append(mSelectedAsset.getDisplayName())
                .append("\n")
                .append(getString(R.string.send_token_amount_text))
                .append(amountText);

        final String finalAddress = address;

        new MaterialDialog.Builder(SendTrc10Activity.this)
                .title(R.string.send_token_title)
                .content(sb.toString())
                .positiveText(R.string.confirm_text)
                .negativeText(R.string.cancel_text)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    String password = mInputPassword.getText().toString();

                    if (Constants.TRON_SYMBOL.equals(mSelectedAsset.getName())) {
                        long amount = (long) (finalAmountDouble * Constants.ONE_TRX);

                        showProgressDialog(null, getString(R.string.loading_msg));
                        mSendTrc10Presenter.sendTron(password, finalAddress, amount);
                    } else {
                        long amount = (long) finalAmountDouble;

                        if (mSelectedAsset.getPrecision() > 0) {
                            amount = (long) (finalAmountDouble * Math.pow(10, mSelectedAsset.getPrecision()));
                        }
                        showProgressDialog(null, getString(R.string.loading_msg));
                        mSendTrc10Presenter.transferAsset(password, finalAddress, mSelectedAsset.getName(), amount);
                    }
                }).show();
    }

    @Override
    public void sendTokenResult(boolean result) {
        hideDialog();

        if (result) {
            if (mFromDonations) {
                new MaterialDialog.Builder(SendTrc10Activity.this)
                        .title(R.string.donation_title_text)
                        .titleColorRes(R.color.colorPrimary)
                        .content(getString(R.string.thanks_msg))
                        .positiveText(R.string.close_text)
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            finishActivity();
                        }).show();
            } else {
                Toast.makeText(SendTrc10Activity.this, getString(R.string.sending_token_success),
                        Toast.LENGTH_SHORT).show();
                finishActivity();
            }
        } else {
            new MaterialDialog.Builder(SendTrc10Activity.this)
                    .title(R.string.send_token_failed_title_text)
                    .titleColorRes(R.color.colorPrimary)
                    .content(getString(R.string.sending_token_failed))
                    .positiveText(R.string.close_text)
                    .onPositive((dialog, which) -> {
                        dialog.dismiss();
                    }).show();
        }
    }

    @Override
    public void invalidPassword() {
        hideDialog();

        Toast.makeText(SendTrc10Activity.this, getString(R.string.invalid_password),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayAccountInfo(List<Asset> assets) {
        mTokenAdapter = new ArrayAdapter<>(SendTrc10Activity.this, android.R.layout.simple_spinner_item,
                assets);

        mTokenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTokenSpinner.setAdapter(mTokenAdapter);
        mTokenSpinner.setOnItemSelectedListener(mTokenItemSelectedListener);

        hideDialog();
    }

    @Override
    public void invalidAddress() {
        hideDialog();

        Toast.makeText(SendTrc10Activity.this, getString(R.string.invalid_address),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionError() {
        hideDialog();

        Toast.makeText(SendTrc10Activity.this, getString(R.string.connection_error_msg),
                Toast.LENGTH_SHORT).show();
    }

    private void startQrScan() {
        Intent qrScanIntent = new Intent(SendTrc10Activity.this, QrScanActivity.class);
        startActivityForResult(qrScanIntent, QR_SCAN_ADDRESS);
    }

    @OnClick(R.id.btn_qrcode_scan)
    public void onQrcodeScanClick() {
        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(SendTrc10Activity.this, getString(R.string.camera_error_msg),
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestPermissions(new String[] { Manifest.permission.CAMERA },
                            CAMERA_PERMISSION_REQ);
                }
            } else {
                startQrScan();
            }
        } else {
            startQrScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkAllPermissionGranted(grantResults)) {
            if (requestCode == CAMERA_PERMISSION_REQ) {
                startQrScan();
            }
        } else {
            Toast.makeText(SendTrc10Activity.this, getString(R.string.camera_error_msg),
                    Toast.LENGTH_SHORT).show();
        }
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
            mSelectedAsset = mTokenAdapter.getItem(pos);
            mInputAmount.setText("");
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> adapterView) {

        }
    };
}
