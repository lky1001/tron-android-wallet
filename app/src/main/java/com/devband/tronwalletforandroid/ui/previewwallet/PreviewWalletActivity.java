package com.devband.tronwalletforandroid.ui.previewwallet;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import butterknife.ButterKnife;

public class PreviewWalletActivity extends CommonActivity implements PreviewWalletView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_wallet);

        ButterKnife.bind(this);
    }
}
