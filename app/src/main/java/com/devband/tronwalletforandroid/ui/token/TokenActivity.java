package com.devband.tronwalletforandroid.ui.token;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronlib.dto.Token;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.ui.more.MoreActivity;
import com.devband.tronwalletforandroid.ui.token.adapter.TokenAdapter;
import com.devband.tronwalletforandroid.ui.vote.VoteActivity;
import com.devband.tronwalletforandroid.ui.vote.VotePresenter;

import org.tron.protos.Protocol;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenActivity extends CommonActivity implements TokenView {

    private static final int PAGE_SIZE = 25;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private TokenAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    private long mStartIndex = 0;

    private boolean mIsLoading;

    private boolean mIsLastPage;

    private AdapterView mAdapterView;

    private long mLoginAccountTrx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_tokens);
        }

        mLayoutManager = new LinearLayoutManager(TokenActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TokenAdapter(TokenActivity.this, mOnItemClickListener, mOnParticipateItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mRecyclerViewOnScrollListener);
        mAdapterView = mAdapter;

        mPresenter = new TokenPresenter(this);
        ((TokenPresenter) mPresenter).setAdapterDataModel(mAdapter);
        mPresenter.onCreate();

        mIsLoading = true;
        ((TokenPresenter) mPresenter).loadItems(mStartIndex, PAGE_SIZE);
    }

    private RecyclerView.OnScrollListener mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!mIsLoading && !mIsLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    mIsLoading = true;
                    ((TokenPresenter) mPresenter).loadItems(mStartIndex, PAGE_SIZE);
                }
            }
        }
    };

    private View.OnClickListener mOnParticipateItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Token item = (Token) v.getTag();

            if (item != null) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(TokenActivity.this)
                        .title(item.getName())
                        .titleColorRes(R.color.colorAccent)
                        .contentColorRes(android.R.color.black)
                        .backgroundColorRes(android.R.color.white)
                        .customView(R.layout.dialog_participate_token, false);

                MaterialDialog dialog = builder.build();

                Button partButton = (Button) dialog.getCustomView().findViewById(R.id.btn_participate);
                EditText inputAmount = (EditText) dialog.getCustomView().findViewById(R.id.input_amount);
                EditText inputPassword = (EditText) dialog.getCustomView().findViewById(R.id.input_password);
                CheckBox agreePartCheckBox = (CheckBox) dialog.getCustomView().findViewById(R.id.agree_participate_token);
                TextView totalText = (TextView) dialog.getCustomView().findViewById(R.id.total_trx_text);
                TextView priceText = (TextView) dialog.getCustomView().findViewById(R.id.price_text);
                TextView yourTrxText = (TextView) dialog.getCustomView().findViewById(R.id.your_trx_text);

                priceText.setText(Utils.getRealTrxFormat(item.getTrxNum()) + " " + Constants.TRON_SYMBOL);
                yourTrxText.setText(Utils.getRealTrxFormat(mLoginAccountTrx) + " " + Constants.TRON_SYMBOL);

                inputAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        long amountBalance = Long.parseLong(inputAmount.getText().toString());
                        totalText.setText(Utils.getRealTrxFormat(amountBalance * item.getPrice()) + " " + Constants.TRON_SYMBOL);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                partButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long tokenAmount = 0;

                        try {
                            tokenAmount = Long.parseLong(inputAmount.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(TokenActivity.this, getString(R.string.invalid_amount),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String password = inputPassword.getText().toString();
                        if (TextUtils.isEmpty(password) || !((VotePresenter) mPresenter).matchPassword(password)) {
                            Toast.makeText(TokenActivity.this, getString(R.string.invalid_password),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        boolean agree = agreePartCheckBox.isChecked();

                        if (!agree) {
                            Toast.makeText(TokenActivity.this, getString(R.string.need_all_agree),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // check trx balance
                        tokenAmount *= item.getPrice();

                        if (tokenAmount > mLoginAccountTrx) {
                            Toast.makeText(TokenActivity.this, getString(R.string.invalid_amount),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        dialog.dismiss();

                        ((TokenPresenter) mPresenter).participateToken(item, tokenAmount);
                    }
                });

                dialog.show();
            }
        }
    };

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mRecyclerView.getChildLayoutPosition(v);
            Token item = mAdapter.getModel(pos);

            Intent intent = new Intent(TokenActivity.this, TokenDetailActivity.class);
            intent.putExtra(TokenDetailActivity.EXTRA_TOKEN_NAME, item.getName());
            startActivity(intent);
        }
    };

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        mIsLoading = false;
        if (!isFinishing()) {
            hideDialog();
            Toast.makeText(TokenActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finishLoading(int total, Protocol.Account account) {
        if (!isFinishing()) {
            mStartIndex += PAGE_SIZE;

            mLoginAccountTrx = account.getBalance();

            if (mStartIndex >= total) {
                mIsLastPage = true;
            }

            mIsLoading = false;
            mAdapterView.refresh();

            hideDialog();
        }
    }

    @Override
    public void participateTokenResult(boolean result) {
        if (!isFinishing()) {
            hideDialog();

            if (result) {
                Toast.makeText(TokenActivity.this, getString(R.string.participate_token_success_msg), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TokenActivity.this, getString(R.string.participate_token_fail_msg), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
