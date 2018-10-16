package com.devband.tronwalletforandroid.ui.vote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.vote.dto.VoteItem;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteListAdapter extends RecyclerView.Adapter<VoteListAdapter.VoteViewHolder> implements AdapterDataModel<VoteItem>, AdapterView {

    private List<VoteItem> mList;

    private Context mContext;

    private View.OnClickListener mVoteClickListener;
    private View.OnClickListener mViewClickListener;

    public VoteListAdapter(Context context, View.OnClickListener viewClickListener, View.OnClickListener voteClickListener) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        this.mVoteClickListener = voteClickListener;
        this.mViewClickListener = viewClickListener;
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vote, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mViewClickListener);
        return new VoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        final VoteItem item = mList.get(position);

        holder.voteNoText.setText(item.getNo() + ".");
        holder.representativeUrlText.setText(item.getUrl());
        holder.representativeUrlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(mContext)
                        .title(R.string.visit_external_site_text)
                        .content(item.getUrl() + " " + mContext.getString(R.string.external_website_warning_msg))
                        .titleColorRes(android.R.color.black)
                        .contentColorRes(android.R.color.black)
                        .backgroundColorRes(android.R.color.white)
                        .positiveText(R.string.visit_site_text)
                        .negativeText(R.string.cancel_text)
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            new FinestWebView.Builder(mContext).show(item.getUrl());
                        }).show();
            }
        });

        holder.representativeAddressText.setText(item.getAddress());
        holder.yourVoteText.setText(Constants.numberFormat.format(item.getMyVoteCount()));
        holder.totalVoteText.setText(Constants.numberFormat.format(item.getTotalVoteCount())
                + " (" + Constants.percentFormat.format(item.getVotesPercentage()) + "%)");
        holder.realtimeVoteText.setText(Constants.numberFormat.format(item.getRealTimeVoteCount()));

        holder.realtimeChangedText.setText(Constants.numberFormat.format(item.getChangeVotes()));

        if (item.getChangeVotes() < 0) {
            holder.realtimeChangedText.setTextColor(mContext.getResources().getColor(R.color.vote_down_color));
        } else if (item.getChangeVotes() > 0) {
            holder.realtimeChangedText.setTextColor(mContext.getResources().getColor(R.color.vote_up_color));
        } else {
            holder.realtimeChangedText.setTextColor(mContext.getResources().getColor(R.color.default_text_color));
        }

        holder.voteProgress.setMax(Constants.VOTE_MAX_PROGRESS);
        // total representative votes
        holder.voteProgress.setSecondaryProgress((float) item.getLastCycleVoteCount() / (float) item.getTotalVoteCount() * Constants.VOTE_MAX_PROGRESS);
        // user votes
        holder.voteProgress.setProgress((float) item.getMyVoteCount() / (float) item.getTotalVoteCount() * Constants.VOTE_MAX_PROGRESS);

        holder.voteButton.setOnClickListener(mVoteClickListener);
        holder.voteButton.setTag(item);

        if (item.isHasTeamPage()) {
            holder.teamPageButton.setVisibility(View.VISIBLE);
            holder.teamPageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FinestWebView.Builder(mContext).show(Constants.SUPER_REPRESENTATIVE_TEAM_PAGE_URL + item.getAddress());
                }
            });
        } else {
            holder.teamPageButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public VoteItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public void add(VoteItem model) {
        mList.add(model);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void addAll(List<VoteItem> list) {
        mList.addAll(list);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public VoteItem getModel(int position) {
        return mList.get(position);
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    public class VoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.vote_no_text)
        TextView voteNoText;

        @BindView(R.id.representative_url_text)
        TextView representativeUrlText;

        @BindView(R.id.representative_address_text)
        TextView representativeAddressText;

        @BindView(R.id.your_vote_text)
        TextView yourVoteText;

        @BindView(R.id.total_votes_text)
        TextView totalVoteText;

        @BindView(R.id.realtime_votes_text)
        TextView realtimeVoteText;

        @BindView(R.id.realtime_changed_text)
        TextView realtimeChangedText;

        @BindView(R.id.progress_votes)
        RoundCornerProgressBar voteProgress;

        @BindView(R.id.representative_vote_button)
        Button voteButton;

        @BindView(R.id.representative_team_page_button)
        Button teamPageButton;

        public VoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
