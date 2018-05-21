package com.devband.tronwalletforandroid.ui.vote.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.vote.dto.VoteItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteListAdapter extends RecyclerView.Adapter<VoteListAdapter.VoteViewHolder> implements AdapterDataModel<VoteItem>, AdapterView {

    private List<VoteItem> mCurrentList;

    private List<VoteItem> mAllList;

    private Context mContext;

    private View.OnClickListener mVoteClickListener;

    private DecimalFormat df = new DecimalFormat("#,##0");
    private DecimalFormat percentDf = new DecimalFormat("#,##0.00");

    public VoteListAdapter(Context mContext, View.OnClickListener voteClickListener) {
        this.mAllList = new ArrayList<>();
        this.mCurrentList = mAllList;
        this.mContext = mContext;
        this.mVoteClickListener = voteClickListener;
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_vote, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
//        v.setOnClickListener(mOnClickListener);
        return new VoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        VoteItem item = mCurrentList.get(position);

        holder.voteNoText.setText(item.getNo() + ".");
        holder.representativeUrlText.setText(item.getUrl());
        holder.representativeAddressText.setText(item.getAddress());
        holder.yourVoteText.setText(df.format(item.getMyVoteCount()));
        holder.totalVoteText.setText(df.format(item.getVoteCount())
                + " (" + percentDf.format(((double) item.getVoteCount() / (double) item.getTotalVoteCount()) * 100f) + "%)");

        long progress = (long) (((double) item.getVoteCount() / (double) item.getTotalVoteCount()) * 10000f);
        holder.voteProgress.setMax(Constants.VOTE_MAX_PROGRESS);
        // total representative votes
        holder.voteProgress.setSecondaryProgress((float) item.getVoteCount() / (float) item.getTotalVoteCount() * Constants.VOTE_MAX_PROGRESS);
        // user votes
        holder.voteProgress.setProgress((float) item.getMyVoteCount() / (float) item.getTotalVoteCount() * Constants.VOTE_MAX_PROGRESS);

        holder.voteButton.setOnClickListener(mVoteClickListener);
        holder.voteButton.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mCurrentList.size();
    }

    @Override
    public void add(VoteItem model) {
        mCurrentList.add(model);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void addAll(List<VoteItem> list) {
        mCurrentList.addAll(list);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void remove(int position) {
        mCurrentList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public VoteItem getModel(int position) {
        return mCurrentList.get(position);
    }

    @Override
    public int getSize() {
        return mCurrentList.size();
    }

    @Override
    public void clear() {
        mCurrentList.clear();
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

        @BindView(R.id.your_votes_text)
        TextView yourVoteText;

        @BindView(R.id.total_votes_text)
        TextView totalVoteText;

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
