package com.devband.tronwalletforandroid.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.ui.intro.IntroActivity;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class CommonActivity extends AppCompatActivity {

    protected BasePresenter mPresenter;

    protected MaterialDialog mMaterialDialog;

    protected void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    protected boolean checkAllPermissionGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    protected void checkLogin() {
        if (!WalletAppManager.getInstance(CommonActivity.this).isLogin()) {
            startActivity(IntroActivity.class);
            finishActivity();
        }
    }

    @Override
    protected void onDestroy() {
        hideDialog();
        super.onDestroy();
    }

    protected void hideDialog() {
        if (mMaterialDialog != null) {
            mMaterialDialog.dismiss();
            mMaterialDialog = null;
        }
    }

    public void finishActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    public void showProgressDialog(@Nullable String title, @NonNull String msg) {
        hideDialog();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        if (!TextUtils.isEmpty(title)) {
            builder.title(title);
        }

        mMaterialDialog = builder
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .content(msg)
                .progress(true,0)
                .canceledOnTouchOutside(false)
                .show();
    }
}
