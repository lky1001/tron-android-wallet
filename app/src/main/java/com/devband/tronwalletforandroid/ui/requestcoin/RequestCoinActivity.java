package com.devband.tronwalletforandroid.ui.requestcoin;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.tron.Tron;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.glxn.qrgen.android.QRCode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RequestCoinActivity extends CommonActivity {

    @Inject
    Tron mTron;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.iv_qrcode)
    public ImageView mQrcodeImageView;

    @BindView(R.id.input_amount)
    EditText mInputAmount;

    @BindView(R.id.btn_gen_qrcode)
    Button mGenQrcodeBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_coin);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_request_token);
        }
    }

    @OnClick(R.id.btn_gen_qrcode)
    public void onGenQrcodeClick() {
        hideDialog();

        showProgressDialog("", getString(R.string.tron_price_title));

        Single.fromCallable(() -> {
            String amount = mInputAmount.getText().toString();

            if (TextUtils.isEmpty(amount)) {
                Toast.makeText(RequestCoinActivity.this, getString(R.string.invalid_amount),
                        Toast.LENGTH_SHORT).show();
                return null;
            }

            double cnt = Double.parseDouble(amount);

            PayInfo payInfo = new PayInfo();
            payInfo.address = mTron.getLoginAddress();
            payInfo.amount = cnt;

            ObjectMapper objectMapper = new ObjectMapper();
            Bitmap payInfoImage = QRCode.from(objectMapper.writeValueAsString(payInfo)).bitmap();

            return payInfoImage;
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Bitmap>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                hideDialog();
                mQrcodeImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable e) {
                hideDialog();
            }
        });
    }

    public static class PayInfo {
        public String address;
        public double amount;
    }
}
