package com.devband.tronwalletforandroid.ui.more;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.devband.tronwalletforandroid.BuildConfig;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.ui.about.AboutActivity;
import com.devband.tronwalletforandroid.ui.opensource.OpenSourceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreActivity extends CommonActivity implements MoreView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tron_app_info_text)
    TextView mTronAppInfoText;

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

        mTronAppInfoText.setText("Tron Android Wallet\nApp Version : v" + BuildConfig.VERSION_NAME);

        mPresenter = new MorePresenter(this);
        mPresenter.onCreate();
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

    @OnClick(R.id.more_about_tron_button)
    public void onAboutTronClick() {
        startActivity(AboutActivity.class);
    }

    @OnClick(R.id.more_node_list_button)
    public void onNodeListClick() {

    }

    @OnClick(R.id.more_witness_list_button)
    public void onWitnessListClick() {

    }

    @OnClick(R.id.more_account_list_button)
    public void onAccountListClick() {

    }

    @OnClick(R.id.more_open_source_button)
    public void onOpenSourceClick() {
        startActivity(OpenSourceActivity.class);
    }

    @OnClick(R.id.more_feedback_button)
    public void onFeedbackClick() {

    }
}
