package com.devband.tronwalletforandroid.ui.qrscan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class QrScanActivity extends CommonActivity {

    public static final String EXTRA_QR_CODE_RESULT = "qr_code_result";

    @BindView(R.id.camera_view)
    SurfaceView mSurfaceView;

    QREader mQrEader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        ButterKnife.bind(this);

        checkCameraPermission();

        mQrEader = new QREader.Builder(this, mSurfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);

                Intent returnIntent = new Intent();
                returnIntent.putExtra(EXTRA_QR_CODE_RESULT, data);
                setResult(Activity.RESULT_OK,returnIntent);
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
        mQrEader.initAndStart(mSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mQrEader.releaseAndCleanup();
    }
}
