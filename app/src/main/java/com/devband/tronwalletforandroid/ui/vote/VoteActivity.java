package com.devband.tronwalletforandroid.ui.vote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.DividerItemDecoration;
import com.devband.tronwalletforandroid.common.CommonActivity;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.tronwalletforandroid.ui.vote.adapter.VoteListAdapter;
import com.devband.tronwalletforandroid.ui.vote.dto.VoteItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoteActivity extends CommonActivity implements VoteView {

    @Inject
    VotePresenter mVotePresenter;
    
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.appbar_layout)
    public AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.listview)
    RecyclerView mVoteListView;

    @BindView(R.id.votes_remaining_count_title_text)
    TextView mVoteRemainingCountTitleText;

    @BindView(R.id.representative_title_text)
    TextView mRepresentativeTitleText;

    @BindView(R.id.representative_count_title_text)
    TextView mRepresentativeCountTitleText;

    @BindView(R.id.total_votes_title_text)
    TextView mTotalVotesTitleText;

    @BindView(R.id.representative_count_text)
    TextView mRepresentativeCountText;

    @BindView(R.id.total_votes_text)
    TextView mTotalVotesText;

    @BindView(R.id.votes_remaining_count_text)
    TextView mVoteRemainingCountText;

    @BindView(R.id.check_my_votes)
    CheckBox mCheckMyVotes;

    @BindView(R.id.retry_button)
    Button mRetryButton;

    private long mRemainVotePoint;
    private long mTotalVotePoint;

    private LinearLayoutManager mLayoutManager;
    private AdapterView mAdapterView;
    private VoteListAdapter mVoteListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mLayoutManager = new LinearLayoutManager(VoteActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mVoteListView.setLayoutManager(mLayoutManager);
        mVoteListView.addItemDecoration(new DividerItemDecoration(0));
        mVoteListView.setNestedScrollingEnabled(false);

        mVoteListAdapter = new VoteListAdapter(VoteActivity.this, mViewClickListener, mVoteClickListener);
        mVoteListView.setAdapter(mVoteListAdapter);
        mAdapterView = mVoteListAdapter;

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mToolbarLayout.setTitle(getString(R.string.title_vote));
                    mRepresentativeTitleText.setVisibility(View.GONE);
                    mRepresentativeCountTitleText.setVisibility(View.GONE);
                    mTotalVotesTitleText.setVisibility(View.GONE);
                    mRepresentativeCountText.setVisibility(View.GONE);
                    mTotalVotesText.setVisibility(View.GONE);
                    mVoteRemainingCountTitleText.setVisibility(View.GONE);
                    mVoteRemainingCountText.setVisibility(View.GONE);
                    isShow = true;
                } else if(isShow) {
                    mToolbarLayout.setTitle("");
                    mRepresentativeTitleText.setVisibility(View.VISIBLE);
                    mRepresentativeCountTitleText.setVisibility(View.VISIBLE);
                    mTotalVotesTitleText.setVisibility(View.VISIBLE);
                    mRepresentativeCountText.setVisibility(View.VISIBLE);
                    mTotalVotesText.setVisibility(View.VISIBLE);
                    mVoteRemainingCountTitleText.setVisibility(View.VISIBLE);
                    mVoteRemainingCountText.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        mCheckMyVotes.setOnClickListener(view -> {
            mVotePresenter.showOnlyMyVotes(mCheckMyVotes.isChecked());
        });

        mVotePresenter.setAdapterDataModel(mVoteListAdapter);
        mVotePresenter.onCreate();
    }

    @Override
    public void showLoadingDialog() {
        if (isFinishing()) {
            return;
        }
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        if (isFinishing()) {
            return;
        }
        mRetryButton.setVisibility(View.VISIBLE);
        mVoteListView.setVisibility(View.GONE);
        hideDialog();
        Toast.makeText(VoteActivity.this, getString(R.string.connection_error_msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayVoteInfo(long totalVotes, long voteItemCount, long myVotePoint, long totalMyVotes) {
        if (isFinishing()) {
            return;
        }
        mRetryButton.setVisibility(View.GONE);
        mVoteListView.setVisibility(View.VISIBLE);

        if (myVotePoint == 0) {
            new MaterialDialog.Builder(VoteActivity.this)
                    .title(getString(R.string.votes_help_text))
                    .content(getString(R.string.votes_help_msg_text))
                    .titleColorRes(R.color.colorAccent)
                    .contentColorRes(android.R.color.black)
                    .backgroundColorRes(android.R.color.white)
                    .positiveText(R.string.close_text)
                    .autoDismiss(true)
                    .build()
                    .show();
        }

        mTotalVotePoint = myVotePoint;
        mRemainVotePoint = myVotePoint - totalMyVotes;

        mTotalVotesText.setText(Constants.numberFormat.format(totalVotes));
        mRepresentativeCountText.setText(Constants.numberFormat.format(voteItemCount));
        mVoteRemainingCountText.setText(Constants.numberFormat.format(mRemainVotePoint));

        mRepresentativeTitleText.setVisibility(View.VISIBLE);
        mRepresentativeCountTitleText.setVisibility(View.VISIBLE);
        mTotalVotesTitleText.setVisibility(View.VISIBLE);
        mRepresentativeCountText.setVisibility(View.VISIBLE);
        mTotalVotesText.setVisibility(View.VISIBLE);
        mVoteRemainingCountTitleText.setVisibility(View.VISIBLE);
        mVoteRemainingCountText.setVisibility(View.VISIBLE);

        hideDialog();
    }

    @Override
    public void successVote() {
        if (isFinishing()) {
            return;
        }
        hideDialog();
        mVotePresenter.getRepresentativeList(mCheckMyVotes.isChecked());
    }

    @Override
    public void refreshList() {
        if (isFinishing()) {
            return;
        }
        mVoteListView.post(new Runnable() {
            @Override
            public void run() {
                mAdapterView.refresh();
            }
        });
    }

    @Override
    public void showInvalidVoteError() {
        if (!isFinishing()) {
            Toast.makeText(VoteActivity.this, getString(R.string.invalid_vote),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.votes_remaining_layout)
    public void onVoteRemainingHelpClick() {
        new MaterialDialog.Builder(VoteActivity.this)
                .title(getString(R.string.votes_remaining_text))
                .content(getString(R.string.votes_remaining_help_text))
                .titleColorRes(android.R.color.black)
                .contentColorRes(android.R.color.black)
                .backgroundColorRes(android.R.color.white)
                .positiveText(R.string.close_text)
                .autoDismiss(true)
                .build()
                .show();
    }

    @OnClick(R.id.retry_button)
    public void onRetryClick() {
        mVotePresenter.getRepresentativeList(mCheckMyVotes.isChecked());
    }

    private View.OnClickListener mViewClickListener = view -> {
        int pos = mVoteListView.getChildLayoutPosition(view);
        VoteItem item = mVoteListAdapter.getItem(pos);
        Intent intent = new Intent(view.getContext(), AccountDetailActivity.class);
        intent.putExtra(AccountDetailActivity.EXTRA_ADDRESS, item.getAddress());
        startActivity(intent);
    };

    private View.OnClickListener mVoteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VoteItem item = (VoteItem) v.getTag();

            if (item != null) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(VoteActivity.this)
                        .title(R.string.title_vote)
                        .titleColorRes(R.color.colorAccent)
                        .contentColorRes(R.color.colorAccent)
                        .backgroundColorRes(android.R.color.white)
                        .customView(R.layout.dialog_vote, false);

                MaterialDialog dialog = builder.build();

                TextView voteUrlText = (TextView) dialog.getCustomView().findViewById(R.id.vote_url_text);
                TextView voteAddressText = (TextView) dialog.getCustomView().findViewById(R.id.vote_address_text);
                Button voteButton = (Button) dialog.getCustomView().findViewById(R.id.btn_vote);
                CheckBox agreeVoteCheckBox = (CheckBox) dialog.getCustomView().findViewById(R.id.agree_vote);
                EditText inputVote = (EditText) dialog.getCustomView().findViewById(R.id.input_vote);
                EditText inputPassword = (EditText) dialog.getCustomView().findViewById(R.id.input_password);
                Button maxButton = (Button) dialog.getCustomView().findViewById(R.id.max_button);
                TextView msgText = (TextView) dialog.getCustomView().findViewById(R.id.voting_warning_text);

                String formatNumber = Utils.getCommaNumber(item.getMyVoteCount());

                inputVote.setText(formatNumber);
                inputVote.setSelection(formatNumber.length());
                voteUrlText.setText(item.getUrl());
                voteAddressText.setText(item.getAddress());

                long otherVote = mTotalVotePoint - item.getMyVoteCount() - mRemainVotePoint;


                inputVote.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (TextUtils.isEmpty(s)) {
                            inputVote.setText("0");
                            inputVote.setSelection(1);
                            return;
                        }

                        long voteBalance = 0;

                        try {
                            voteBalance = Long.parseLong(s.toString().replaceAll(",", ""));
                        } catch (NumberFormatException e) {
                            return;
                        }

                        msgText.setVisibility(View.INVISIBLE);

                        // least 1
                        if (voteBalance == 0 && otherVote == 0) {
                            msgText.setText(getString(R.string.warning_vote_least_one));
                            msgText.setVisibility(View.VISIBLE);
                            return;
                        }

                        // warning over total
                        if (voteBalance > mTotalVotePoint) {
                            inputVote.setText(String.valueOf(mTotalVotePoint));
                            voteBalance = mTotalVotePoint;
                        }

                        // warning cancel other vote
                        if (voteBalance > (item.getMyVoteCount() + mRemainVotePoint)) {
                            msgText.setText(getString(R.string.warning_cancel_other_votes));
                            msgText.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        inputVote.removeTextChangedListener(this);

                        if (TextUtils.isEmpty(s)) {
                            inputVote.setText("0");
                            inputVote.setSelection(1);
                            inputVote.addTextChangedListener(this);
                            return;
                        }

                        String number = s.toString().replaceAll(",", "");

                        String formatNumber = Utils.getCommaNumber(Long.parseLong(number));

                        inputVote.setText(formatNumber);

                        inputVote.setSelection(formatNumber.length());

                        inputVote.addTextChangedListener(this);
                    }
                });

                voteButton.setOnClickListener(view -> {
                    long voteBalance = 0;

                    try {
                        String number = inputVote.getText().toString().replaceAll(",", "");
                        voteBalance = Long.parseLong(number);
                    } catch (NumberFormatException e) {
                        Toast.makeText(VoteActivity.this, getString(R.string.invalid_vote),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (voteBalance > mTotalVotePoint) {
                        Toast.makeText(VoteActivity.this, getString(R.string.invalid_vote),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (voteBalance == 0 && otherVote == 0) {
                        Toast.makeText(VoteActivity.this, getString(R.string.invalid_vote),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String password = inputPassword.getText().toString();
                    if (TextUtils.isEmpty(password) || !mVotePresenter.matchPassword(password)) {
                        Toast.makeText(VoteActivity.this, getString(R.string.invalid_password),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean agree = agreeVoteCheckBox.isChecked();

                    if (!agree) {
                        Toast.makeText(VoteActivity.this, getString(R.string.need_all_agree),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dialog.dismiss();

                    if (voteBalance > (item.getMyVoteCount() + mRemainVotePoint)) {
                        mVotePresenter.voteRepresentative(password, item.getAddress(), voteBalance, false);
                    } else {
                        mVotePresenter.voteRepresentative(password, item.getAddress(), voteBalance, true);
                    }
                });

                maxButton.setOnClickListener(view -> {
                    inputVote.setText(String.valueOf(mTotalVotePoint));
                });

                dialog.show();
            }
        }
    };
}
