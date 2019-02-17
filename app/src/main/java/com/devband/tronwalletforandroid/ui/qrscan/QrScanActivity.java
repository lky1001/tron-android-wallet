package com.devband.tronwalletforandroid.ui.qrscan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.requestcoin.RequestCoinActivity;
import com.devband.tronwalletforandroid.ui.sendtrc10.SendTrc10Activity;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class QrScanActivity extends CommonActivity {

    public static final String EXTRA_QR_CODE_ADDRESS = "qr_code_address";
    public static final String EXTRA_QR_CODE_AMOUNT = "qr_code_amount";
    public static final String EXTRA_FROM_TRON_PAY_MENU = "from_tron_pay_menu";

    @BindView(R.id.camera_view)
    SurfaceView mSurfaceView;

    private QREader mQrEader;

    private boolean mFromTronPayMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        ButterKnife.bind(this);

        mFromTronPayMenu = getIntent().getBooleanExtra(EXTRA_FROM_TRON_PAY_MENU, false);

        initQrEader();
    }

    private void initQrEader() {
        mQrEader = new QREader.Builder(this, mSurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Intent intent;

                if (mFromTronPayMenu) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    RequestCoinActivity.PayInfo payInfo;

                    try {
                        payInfo = objectMapper.readValue(data, RequestCoinActivity.PayInfo.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        payInfo = new RequestCoinActivity.PayInfo();
                    }

                    intent = new Intent(QrScanActivity.this, SendTrc10Activity.class);
                    intent.putExtra(EXTRA_QR_CODE_ADDRESS, payInfo.address);
                    intent.putExtra(EXTRA_QR_CODE_AMOUNT, payInfo.amount);
                    intent.putExtra(EXTRA_FROM_TRON_PAY_MENU, mFromTronPayMenu);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.putExtra(EXTRA_QR_CODE_ADDRESS, data);
                    intent.putExtra(EXTRA_QR_CODE_AMOUNT, 0);
                    setResult(Activity.RESULT_OK, intent);
                }

                finishActivity();
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(mSurfaceView.getHeight())
                .width(mSurfaceView.getWidth())
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mQrEader != null) {
            mQrEader.initAndStart(mSurfaceView);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mQrEader != null) {
            mQrEader.releaseAndCleanup();
        }
    }
}
