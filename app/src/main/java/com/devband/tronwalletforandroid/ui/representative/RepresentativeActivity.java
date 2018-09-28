package com.devband.tronwalletforandroid.ui.representative;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.ui.representative.adapter.RepresentativeListAdapter;
import com.devband.tronwalletforandroid.ui.representative.dto.Representative;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepresentativeActivity extends CommonActivity implements RepresentativeView {

    @Inject
    RepresentativePresenter mRepresentativePresenter;
    
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar_layout)
    public AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.listview)
    RecyclerView mRepresentativeListView;

    @BindView(R.id.representative_title_text)
    TextView mRepresentativeTitleText;

    @BindView(R.id.representative_count_title_text)
    TextView mRepresentativeCountTitleText;

    @BindView(R.id.highest_votes_title_text)
    TextView mHighestVotesTitleText;

    @BindView(R.id.representative_count_text)
    TextView mRepresentativeCountText;

    @BindView(R.id.highest_votes_text)
    TextView mHighestVotesText;

    @BindView(R.id.retry_button)
    Button mRetryButton;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private RepresentativeListAdapter mRepresentativeListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mLayoutManager = new LinearLayoutManager(RepresentativeActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRepresentativeListView.setLayoutManager(mLayoutManager);
        mRepresentativeListView.addItemDecoration(new DividerItemDecoration(0));
        mRepresentativeListView.setNestedScrollingEnabled(false);

        mRepresentativeListAdapter = new RepresentativeListAdapter(RepresentativeActivity.this, mOnListItemClickListener);
        mRepresentativeListView.addItemDecoration(new StickyRecyclerHeadersDecoration(mRepresentativeListAdapter));

        mRepresentativeListView.setAdapter(mRepresentativeListAdapter);
        mAdapterView = mRepresentativeListAdapter;

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mToolbarLayout.setTitle(getString(R.string.title_representative_list));
                    mRepresentativeTitleText.setVisibility(View.GONE);
                    mRepresentativeCountTitleText.setVisibility(View.GONE);
                    mHighestVotesTitleText.setVisibility(View.GONE);
                    mRepresentativeCountText.setVisibility(View.GONE);
                    mHighestVotesText.setVisibility(View.GONE);
                    isShow = true;
                } else if(isShow) {
                    mToolbarLayout.setTitle("");
                    mRepresentativeTitleText.setVisibility(View.VISIBLE);
                    mRepresentativeCountTitleText.setVisibility(View.VISIBLE);
                    mHighestVotesTitleText.setVisibility(View.VISIBLE);
                    mRepresentativeCountText.setVisibility(View.VISIBLE);
                    mHighestVotesText.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        mRepresentativePresenter.setAdapterDataModel(mRepresentativeListAdapter);
        mRepresentativePresenter.onCreate();
    }

    private View.OnClickListener mOnListItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mRepresentativeListView.getChildLayoutPosition(v);
            if (pos > RecyclerView.NO_POSITION) {
                final Representative item = mRepresentativeListAdapter.getModel(pos);

                new MaterialDialog.Builder(RepresentativeActivity.this)
                        .title(R.string.title_vote_representative)
                        .titleColorRes(R.color.colorAccent)
                        .contentColorRes(R.color.colorAccent)
                        .backgroundColorRes(android.R.color.white)
                        .inputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
                        .input(getString(R.string.vote_amount_text), "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                dialog.dismiss();
                                String amt = input.toString();
                                long amount = 0;

                                try {
                                    amount = Long.parseLong(amt);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                                if (amount > 0) {
                                    inputPassword(item, amount);
                                } else {
                                    Toast.makeText(RepresentativeActivity.this, getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).show();
            }
        }
    };

    private void inputPassword(Representative item, long amount) {
        new MaterialDialog.Builder(this)
                .title(R.string.title_vote_representative)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(R.color.colorAccent)
                .backgroundColorRes(android.R.color.white)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input(getString(R.string.input_password_text), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        dialog.dismiss();
                        String password = input.toString();

                        if (!TextUtils.isEmpty(password) && mRepresentativePresenter.matchPassword(password)) {
                            confirmVote(item, amount);
                        } else {
                            Toast.makeText(RepresentativeActivity.this, getString(R.string.invalid_password),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    private void confirmVote(Representative item, long amount) {
        StringBuilder sb = new StringBuilder();

        sb.append(getString(R.string.vote_representative_warning_msg))
                .append("\n\n")
                .append(getString(R.string.vote_representative_name_text))
                .append(item.getUrl())
                .append("\n")
                .append(getString(R.string.vote_representative_amount_text))
                .append(Constants.numberFormat.format(amount) + " " + Constants.ONE_TRX);

        new MaterialDialog.Builder(RepresentativeActivity.this)
                .title(R.string.send_token_title)
                .content(sb.toString())
                .positiveText(R.string.confirm_text)
                .negativeText(R.string.cancel_text)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    showProgressDialog(null, getString(R.string.loading_msg));
                }).show();
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void displayRepresentativeInfo(int witnessCount, long highestVotes) {
        mRetryButton.setVisibility(View.GONE);

        mRepresentativeCountText.setText(Constants.numberFormat.format(witnessCount));
        mHighestVotesText.setText(Constants.numberFormat.format(highestVotes) + " " + Constants.TRON_SYMBOL);

        mRepresentativeCountTitleText.setVisibility(View.VISIBLE);
        mHighestVotesTitleText.setVisibility(View.VISIBLE);
        mRepresentativeCountText.setVisibility(View.VISIBLE);
        mHighestVotesText.setVisibility(View.VISIBLE);
        hideDialog();
    }

    @Override
    public void showServerError() {
        mRetryButton.setVisibility(View.VISIBLE);
        hideDialog();
        Toast.makeText(RepresentativeActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.retry_button)
    public void onRetryClick() {
        mRepresentativePresenter.getRepresentativeList();
    }
}
