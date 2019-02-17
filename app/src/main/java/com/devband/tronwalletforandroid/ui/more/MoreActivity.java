package com.devband.tronwalletforandroid.ui.more;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.BuildConfig;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.about.AboutActivity;
import com.devband.tronwalletforandroid.ui.market.MarketActivity;
import com.devband.tronwalletforandroid.ui.node.NodeActivity;
import com.devband.tronwalletforandroid.ui.opensource.OpenSourceActivity;
import com.devband.tronwalletforandroid.ui.representative.RepresentativeActivity;
import com.devband.tronwalletforandroid.ui.sendtrc10.SendTrc10Activity;
import com.marcoscg.fingerauth.FingerAuth;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MoreActivity extends CommonActivity implements MoreView {

    @Inject
    MorePresenter mMorePresenter;

    @Inject
    Tron mTron;

    @Inject
    CustomPreference mCustomPreference;

    public static final String EXTRA_FROM_DONATIONS = "from_donations";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tron_app_info_text)
    TextView mTronAppInfoText;

    @BindView(R.id.more_fingerprint_checkbox)
    CheckBox mFingerprintAuthCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_more);
        }

        boolean hasFingerprintSupport = FingerAuth.hasFingerprintSupport(this);

        if (hasFingerprintSupport) {
            Single.fromCallable(() -> mCustomPreference.getUseFingerprint())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Boolean useFingerprint) {
                            mFingerprintAuthCheckBox.setChecked(useFingerprint);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

            mFingerprintAuthCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mCustomPreference.setUseFingerprint(isChecked);
            });

            mFingerprintAuthCheckBox.setEnabled(true);
        } else {
            mFingerprintAuthCheckBox.setEnabled(false);
        }

        mTronAppInfoText.setText("Tron Wallet for Android\nApp Version : v" + BuildConfig.VERSION_NAME);

        mMorePresenter.onCreate();
    }

    @OnClick(R.id.more_fingerprint_button)
    public void onFingerprintClick() {
        boolean checked = mFingerprintAuthCheckBox.isChecked();

        mFingerprintAuthCheckBox.setChecked(!checked);

        mCustomPreference.setUseFingerprint(!checked);
    }

    @OnClick(R.id.more_about_tron_button)
    public void onAboutTronClick() {
        startActivity(AboutActivity.class);
    }

    @OnClick(R.id.more_markets_button)
    public void onMarketsClick() {
        startActivity(MarketActivity.class);
    }

    @OnClick(R.id.more_node_list_button)
    public void onNodeListClick() {
        startActivity(NodeActivity.class);
    }

    @OnClick(R.id.more_representative_list_button)
    public void onWitnessListClick() {
        startActivity(RepresentativeActivity.class);
    }

    @OnClick(R.id.more_open_source_button)
    public void onOpenSourceClick() {
        startActivity(OpenSourceActivity.class);
    }

    @OnClick(R.id.more_feedback_button)
    public void onFeedbackClick() {
        String title = getString(R.string.string_feedback_email_title);
        StringBuilder body = new StringBuilder();
        body.append("\n\n\n\n\n");
        body.append(getString(R.string.string_feedback_email_body1));
        body.append(BuildConfig.VERSION_NAME);
        body.append("\n");
        body.append(getString(R.string.string_feedback_email_body2));
        body.append(Build.VERSION.RELEASE);
        body.append("\n");
        body.append(getString(R.string.string_feedback_email_body3));
        body.append(Build.MANUFACTURER).append(", ")
                .append(Build.BRAND).append(", ")
                .append(Build.MODEL);

        sendMailUsingGmail(getResources().getStringArray(R.array.feedback_email_recipient),
                title, body.toString());
    }

    @OnClick(R.id.more_donations_button)
    public void onDonationsClick() {
        Intent intent = new Intent(MoreActivity.this, SendTrc10Activity.class);
        intent.putExtra(EXTRA_FROM_DONATIONS, true);
        startActivity(intent);
    }

    @OnClick(R.id.more_custom_node_button)
    public void onCustomFullNodeClick() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.title_custom_fullnode)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .customView(R.layout.dialog_custom_fullnode, false);

        MaterialDialog dialog = builder.build();

        Button usingButton = (Button) dialog.getCustomView().findViewById(R.id.btn_using);
        EditText inputHost = (EditText) dialog.getCustomView().findViewById(R.id.input_host);
        EditText inputPort = (EditText) dialog.getCustomView().findViewById(R.id.input_port);

        String savedHost = mCustomPreference.getCustomFullNodeHost();

        if (!TextUtils.isEmpty(savedHost)) {
            String[] tmp = savedHost.split(":");
            if (tmp.length == 2) {
                inputHost.setText(tmp[0]);
                inputPort.setText(tmp[1]);
            }
        }

        usingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = inputHost.getText().toString();
                String port = inputPort.getText().toString();

                if (!TextUtils.isEmpty(host) && !TextUtils.isEmpty(port)) {
                    mCustomPreference.setCustomFullNodeHost(host + ":" + port);
                } else if (TextUtils.isEmpty(host) && TextUtils.isEmpty(port)) {
                    mCustomPreference.setCustomFullNodeHost("");
                } else {
                    Toast.makeText(MoreActivity.this, getString(R.string.invalid_host),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    mTron.initTronNode();
                } catch (Exception e) {
                    mCustomPreference.setCustomFullNodeHost("");
                    Toast.makeText(MoreActivity.this, getString(R.string.invalid_host),
                            Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
