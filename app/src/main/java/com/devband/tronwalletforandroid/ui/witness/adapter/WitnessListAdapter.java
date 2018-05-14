package com.devband.tronwalletforandroid.ui.witness.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.main.adapter.AdapterDataModel;
import com.devband.tronwalletforandroid.ui.main.adapter.AdapterView;
import com.devband.tronwalletforandroid.ui.witness.dto.Witness;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WitnessListAdapter extends RecyclerView.Adapter<WitnessListAdapter.WitnessViewHolder> implements AdapterDataModel<Witness>, AdapterView {

    private List<Witness> mList;

    private Context mContext;

    private DecimalFormat df = new DecimalFormat("#,##0");

    public WitnessListAdapter(Context mContext) {
        this.mList = new ArrayList<>();
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public WitnessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_witness, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        return new WitnessViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WitnessViewHolder holder, int position) {
        Witness item = mList.get(position);

        holder.witnessNoText.setText((position + 1) + ".");
        holder.witnessUrlText.setText(item.getUrl());
        holder.witnessVotesText.setText(df.format(item.getVoteCount()) + Constants.TRON_SYMBOL);
        holder.latestBlockText.setText(df.format(item.getLatestBlockNum()));
        holder.producedBlockText.setText(df.format(item.getTotalProduced()));
        holder.missedBlockText.setText(df.format(item.getTotalMissed()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(Witness model) {
        mList.add(model);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void addAll(List<Witness> list) {
        mList.addAll(list);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public Witness getModel(int position) {
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

    public class WitnessViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.witness_vote_button)
        Button witnessVoteButton;

        @BindView(R.id.witness_no_text)
        TextView witnessNoText;

        @BindView(R.id.witness_url_text)
        TextView witnessUrlText;

        @BindView(R.id.witness_votes_text)
        TextView witnessVotesText;

        @BindView(R.id.latest_block_text)
        TextView latestBlockText;

        @BindView(R.id.produced_block_text)
        TextView producedBlockText;

        @BindView(R.id.missed_block_text)
        TextView missedBlockText;

        public WitnessViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
