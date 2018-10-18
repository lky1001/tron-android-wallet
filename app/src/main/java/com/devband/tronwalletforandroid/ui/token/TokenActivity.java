package com.devband.tronwalletforandroid.ui.token;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.ui.token.adapter.TokenAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.tron.protos.Protocol;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TokenActivity extends CommonActivity implements TokenView {

    private static final int PAGE_SIZE = 25;
    
    @Inject
    TokenPresenter mTokenPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    View mEmptyView;

    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

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

        mSearchView.setOnQueryTextListener(mOnQueryTextListener);
        mSearchView.setOnSearchViewListener(mOnSearchViewListener);

        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        mLayoutManager = new LinearLayoutManager(TokenActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TokenAdapter(TokenActivity.this, mOnItemClickListener, mOnParticipateItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mRecyclerViewOnScrollListener);
        mAdapterView = mAdapter;

        mTokenPresenter.setAdapterDataModel(mAdapter);
        mTokenPresenter.onCreate();

        mIsLoading = true;
        mTokenPresenter.loadItems(mStartIndex, PAGE_SIZE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.token_activity_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search_token);
        mSearchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            mStartIndex = 0;
            mIsLoading = true;
            mTokenPresenter.clearData();
            mAdapterView.refresh();
            mSwipeRefreshLayout.setRefreshing(true);
            mSearchView.closeSearch();
            mTokenPresenter.loadItems(mStartIndex, PAGE_SIZE);
        }
    };

    private MaterialSearchView.OnQueryTextListener mOnQueryTextListener = new MaterialSearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mStartIndex = 0;
            mIsLoading = true;
            mTokenPresenter.clearData();
            mAdapterView.refresh();
            mSearchView.closeSearch();
            if (!TextUtils.isEmpty(query)) {
                mTokenPresenter.findToken(query, mStartIndex, PAGE_SIZE);
            } else {
                mTokenPresenter.loadItems(mStartIndex, PAGE_SIZE);
            }
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    private MaterialSearchView.SearchViewListener mOnSearchViewListener = new MaterialSearchView.SearchViewListener() {

        @Override
        public void onSearchViewShown() {

        }

        @Override
        public void onSearchViewClosed() {

        }
    };

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
                    mTokenPresenter.loadItems(mStartIndex, PAGE_SIZE);
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

                priceText.setText(Utils.getRealTrxFormat(item.getPrice()) + " " + Constants.TRON_SYMBOL);
                yourTrxText.setText(Utils.getRealTrxFormat(mLoginAccountTrx) + " " + Constants.TRON_SYMBOL);

                inputAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            if (!TextUtils.isEmpty(inputAmount.getText().toString())) {
                                long amountBalance = Long.parseLong(inputAmount.getText().toString());
                                totalText.setText(Utils.getRealTrxFormat(amountBalance * item.getPrice()) + " " + Constants.TRON_SYMBOL);
                            } else {
                                totalText.setText("0 " + Constants.TRON_SYMBOL);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
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
                        if (TextUtils.isEmpty(password) || !mTokenPresenter.matchPassword(password)) {
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

                        if (tokenAmount == 0 || tokenAmount > mLoginAccountTrx) {
                            Toast.makeText(TokenActivity.this, getString(R.string.invalid_amount),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        dialog.dismiss();

                        mTokenPresenter.participateToken(password, item, tokenAmount);
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
            mSwipeRefreshLayout.setRefreshing(false);
            hideDialog();
            Toast.makeText(TokenActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finishLoading(int total, Protocol.Account account) {
        if (!isFinishing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            boolean isFirstLoading = mStartIndex == 0;

            mStartIndex += PAGE_SIZE;

            mLoginAccountTrx = account.getBalance();

            if (mStartIndex >= total) {
                mIsLastPage = true;
            }

            mIsLoading = false;
            mAdapterView.refresh();

            if (isFirstLoading && total == 0) {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);

            } else {
                if (mRecyclerView.getVisibility() != View.VISIBLE) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                if (mEmptyView.getVisibility() == View.VISIBLE) {
                    mEmptyView.setVisibility(View.GONE);
                }
            }

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

    @Override
    public void needLogin() {
        finishActivity();
    }
}
