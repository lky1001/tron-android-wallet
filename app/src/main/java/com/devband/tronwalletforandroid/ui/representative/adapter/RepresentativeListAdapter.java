package com.devband.tronwalletforandroid.ui.representative.adapter;

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
import com.devband.tronwalletforandroid.ui.representative.dto.Representative;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepresentativeListAdapter extends RecyclerView.Adapter<RepresentativeListAdapter.RepresentativeViewHolder> implements AdapterDataModel<Representative>, AdapterView {

    private List<Representative> mList;

    private Context mContext;

    private View.OnClickListener mOnClickListener;

    private DecimalFormat df = new DecimalFormat("#,##0");
    private DecimalFormat percentDf = new DecimalFormat("#,##0.00");

    public RepresentativeListAdapter(Context mContext, View.OnClickListener onClickListener) {
        this.mList = new ArrayList<>();
        this.mContext = mContext;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RepresentativeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_representative, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
//        v.setOnClickListener(mOnClickListener);
        return new RepresentativeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RepresentativeViewHolder holder, int position) {
        Representative item = mList.get(position);

        holder.representativeNoText.setText((position + 1) + ".");
        holder.representativeUrlText.setText(item.getUrl());
        holder.representativeVotesText.setText(df.format(item.getVoteCount()) + Constants.TRON_SYMBOL);
        holder.latestBlockText.setText(df.format(item.getLatestBlockNum()));
        holder.producedBlockText.setText(df.format(item.getTotalProduced()));
        holder.missedBlockText.setText(df.format(item.getTotalMissed()));
        holder.productivityText.setText(percentDf.format(item.getProductivity() * 100) + "%");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(Representative model) {
        mList.add(model);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void addAll(List<Representative> list) {
        mList.addAll(list);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public Representative getModel(int position) {
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

    public class RepresentativeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.representative_no_text)
        TextView representativeNoText;

        @BindView(R.id.representative_url_text)
        TextView representativeUrlText;

        @BindView(R.id.representative_votes_text)
        TextView representativeVotesText;

        @BindView(R.id.latest_block_text)
        TextView latestBlockText;

        @BindView(R.id.produced_block_text)
        TextView producedBlockText;

        @BindView(R.id.missed_block_text)
        TextView missedBlockText;

        @BindView(R.id.productivity_text)
        TextView productivityText;

        public RepresentativeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
