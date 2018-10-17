package com.devband.tronwalletforandroid.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;

import java.lang.reflect.Field;
import java.util.List;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class CommonActivity extends DaggerAppCompatActivity {

    protected MaterialDialog mMaterialDialog;
    private CompositeDisposable mAllDisposables = new CompositeDisposable();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

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

    protected void addDisposable(Disposable disposable) {
        mAllDisposables.add(disposable);
    }

    @Override
    protected void onDestroy() {
        hideDialog();
        mAllDisposables.clear();
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

    @SuppressLint("DefaultLocale")
    public void sendMailUsingGmail(String[] receiver, String emailTitle, String emailBody) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.putExtra(Intent.EXTRA_EMAIL, receiver);
        // intent.putExtra(Intent.EXTRA_CC, ccs);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailTitle);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        emailIntent.setType("message/rfc882");

        PackageManager pm = getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;

        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.endsWith(".gm")
                    || info.activityInfo.name.toLowerCase().contains("gmail")) {
                best = info;
                break;
            }
        }

        if (best != null) {
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            startActivity(emailIntent);
        } else {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.string_select_email_app)));
        }
    }

    @SuppressWarnings("RestrictedApi")
    protected void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }
}
