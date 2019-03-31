package com.devband.tronwalletforandroid.ui.sendtrc20;

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
import com.devband.tronwalletforandroid.database.model.Trc20ContractModel;
import com.devband.tronwalletforandroid.ui.qrscan.QrScanActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendTrc20Activity extends CommonActivity implements SendTrc20View {

    @Inject
    SendTrc20Presenter mSendTrc20Presenter;

    private static final int CAMERA_PERMISSION_REQ = 9929;
    public static final int QR_SCAN_ADDRESS = 3321;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.input_balance)
    public EditText mInputBalance;

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

    private ArrayAdapter<TokenContract> mTokenAdapter;

    private TokenContract mSelectedAsset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_trc20_token);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_send_trc20_token);
        }

        mSendTrc20Presenter.onCreate();
    }

    @Override
    protected void onResume() {
        showProgressDialog(null, getString(R.string.loading_msg));
        mSendTrc20Presenter.onResume();
        super.onResume();
    }

    @OnClick(R.id.btn_refresh)
    public void onRefreshClick() {

    }

    private void startQrScan() {
        Intent qrScanIntent = new Intent(SendTrc20Activity.this, QrScanActivity.class);
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
                    Toast.makeText(SendTrc20Activity.this, getString(R.string.camera_error_msg),
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
            Toast.makeText(SendTrc20Activity.this, getString(R.string.camera_error_msg),
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

    @Override
    public void showLoadingDialog() {
        if (!isFinishing()) {
            showProgressDialog(null, getString(R.string.loading_msg));
        }
    }

    @Override
    public void setTrc20List(List<Trc20ContractModel> trc20ContractModels) {
        List<TokenContract> tokenContracts = new ArrayList<>();

        for (Trc20ContractModel trc20ContractModel : trc20ContractModels) {
            tokenContracts.add(TokenContract.builder()
                    .name(trc20ContractModel.getName())
                    .contractAddress(trc20ContractModel.getAddress())
                    .precision(trc20ContractModel.getPrecision())
                    .displayName(trc20ContractModel.getName())
                    .build());
        }

        mTokenAdapter = new ArrayAdapter<>(SendTrc20Activity.this, android.R.layout.simple_spinner_item,
                tokenContracts);

        mTokenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTokenSpinner.setAdapter(mTokenAdapter);
        mTokenSpinner.setOnItemSelectedListener(mTokenItemSelectedListener);

        hideDialog();
    }

    @Override
    public void success() {

    }

    @Override
    public void failed() {

    }

    @Override
    public void invalidPassword() {

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

    @OnClick(R.id.btn_send_trx)
    public void onSendTrxClick() {
        String address = mInputAddress.getText().toString();
        address = address.trim();

        if (address.isEmpty()) {
            Toast.makeText(SendTrc20Activity.this, getString(R.string.invalid_address),
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
            Toast.makeText(SendTrc20Activity.this, getString(R.string.invalid_amount),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountDouble <= 0 || amountDouble > mSelectedAsset.getBalance()) {
            Toast.makeText(SendTrc20Activity.this, getString(R.string.invalid_amount),
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

        new MaterialDialog.Builder(SendTrc20Activity.this)
                .title(R.string.send_token_title)
                .content(sb.toString())
                .positiveText(R.string.confirm_text)
                .negativeText(R.string.cancel_text)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    String password = mInputPassword.getText().toString();

                    long amount = (long) finalAmountDouble;

                    if (mSelectedAsset.getPrecision() > 0) {
                        amount = (long) (finalAmountDouble * Math.pow(10, mSelectedAsset.getPrecision()));
                    }

                    showProgressDialog(null, getString(R.string.loading_msg));
                    mSendTrc20Presenter.transferAsset(password, finalAddress, mSelectedAsset.getName(), amount);
                }).show();
    }
}
