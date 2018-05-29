package com.devband.tronwalletforandroid.ui.mytransfer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.mytransfer.adapter.TransferAdapter;
import com.devband.tronwalletforandroid.ui.mytransfer.dto.TransferInfo;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 17..
 */

public class TransferActivity extends CommonActivity implements TransferView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private TransferAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transfer);

        ButterKnife.bind(this);

        initUi();

        mPresenter = new TransferPresenter(this);
        mPresenter.onCreate();
    }

    private void initUi() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_transfer_text);
        }

        mAdapter = new TransferAdapter(TransferActivity.this, mOnItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int pos = mRecyclerView.getChildLayoutPosition(v);
            TransferInfo item = mAdapter.getItem(pos);

            MaterialDialog.Builder builder = new MaterialDialog.Builder(TransferActivity.this)
                    .title(R.string.title_transfer_text)
                    .titleColorRes(android.R.color.black)
                    .contentColorRes(android.R.color.black)
                    .backgroundColorRes(android.R.color.white)
                    .customView(R.layout.dialog_transfer, false)
                    .positiveText(R.string.close_text);

            MaterialDialog dialog = builder.build();

            TextView hashText = (TextView) dialog.getCustomView().findViewById(R.id.hash_text);
            TextView blockText = (TextView) dialog.getCustomView().findViewById(R.id.block_text);
            TextView sendText = (TextView) dialog.getCustomView().findViewById(R.id.send_address_text);
            TextView toText = (TextView) dialog.getCustomView().findViewById(R.id.to_address_text);
            TextView statusText = (TextView) dialog.getCustomView().findViewById(R.id.status_text);
            TextView amountText = (TextView) dialog.getCustomView().findViewById(R.id.amount_text);
            TextView dateText = (TextView) dialog.getCustomView().findViewById(R.id.date_text);


            long amount = item.getAmount();

            if (item.getTokenName().equalsIgnoreCase(Constants.TRON_SYMBOL)) {
                amount = (long) (amount / Constants.ONE_TRX);
            }

            hashText.setText(item.getHash());
            blockText.setText(Constants.numberFormat.format(item.getBlock()));
            sendText.setText(item.getTransferFromAddress());
            toText.setText(item.getTransferToAddress());
            statusText.setText(item.isConfirmed() ? getString(R.string.confirmed_text) : getString(R.string.unconfirmed_text));
            amountText.setText(Constants.tronBalanceFormat.format(amount) + " " + item.getTokenName());
            dateText.setText(Constants.sdf.format(new Date(item.getTimestamp())));

            dialog.show();
        }
    };

    @Override
    public void transferDataLoadSuccess(List<TransferInfo> TransferInfos) {
        hideDialog();
        if (mAdapter != null) {
            mAdapter.refresh(TransferInfos);
            getSupportActionBar().setTitle(getString(R.string.title_transfer_text)
                    + "(" + Constants.numberFormat.format(TransferInfos.size()) + ")");
        }
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        hideDialog();
        Toast.makeText(TransferActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }
}
