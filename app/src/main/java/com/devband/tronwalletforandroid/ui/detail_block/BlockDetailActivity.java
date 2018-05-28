package com.devband.tronwalletforandroid.ui.detail_block;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronlib.dto.TransactionStats;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.opencensus.internal.StringUtil;

/**
 * Created by user on 2018. 5. 28..
 */

public class BlockDetailActivity extends CommonActivity implements DetailBlockView {

    public static final String KEY_ADDRESS = "key_address";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.txt_address_value)
    TextView mTxtAddress;

    @BindView(R.id.txt_stats_in)
    TextView mTxtStatsIn;
    @BindView(R.id.txt_stats_out)
    TextView mTxtStatsOut;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_block);
        ButterKnife.bind(this);
        initUi();

        mPresenter = new DetailBlockPresenter(this);
        dataLoad();
    }

    private void dataLoad() {
        String address = getStringIntentData(KEY_ADDRESS);
        if (TextUtils.isEmpty(address)) {
            //TODO SHOW ERROR DIALOG
            return;
        }
        ((DetailBlockPresenter) mPresenter).loadTransactionStats(address);
    }

    private String getStringIntentData(String key) {
        return getIntent().getStringExtra(key);
    }

    private void initUi() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_detail_block);
        }

    }

    @Override
    public void transactionStatsLoadSuccess(TransactionStats data) {
        hideDialog();
        if (mTxtAddress == null || mTxtStatsIn == null || mTxtStatsOut == null) {
            return;
        }
        String address = getStringIntentData(KEY_ADDRESS);
        long in = data.getTransactionsIn();
        long out = data.getTransactionsOut();

        mTxtAddress.setText(address);
        mTxtStatsIn.setText(in + "");
        mTxtStatsOut.setText(out + "");
    }

    @Override
    public void showLoadingDilaog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }
}
