package com.devband.tronwalletforandroid.ui.representative.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.representative.dto.Representative;
import com.thefinestartist.finestwebview.FinestWebView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepresentativeListAdapter extends RecyclerView.Adapter<RepresentativeListAdapter.RepresentativeViewHolder>
        implements AdapterDataModel<Representative>, AdapterView, StickyRecyclerHeadersAdapter<RepresentativeListAdapter.RepresentativeHeaderHolder> {

    private List<Representative> mList;

    private Context mContext;

    private View.OnClickListener mOnClickListener;

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

        holder.representativeVotesText.setText(Constants.numberFormat.format(item.getVoteCount()) + Constants.TRON_SYMBOL);
        holder.latestBlockText.setText(Constants.numberFormat.format(item.getLatestBlockNum()));
        holder.producedBlockText.setText(Constants.numberFormat.format(item.getTotalProduced()));
        holder.missedBlockText.setText(Constants.numberFormat.format(item.getTotalMissed()));
        holder.productivityText.setText(Constants.percentFormat.format(item.getProductivity() * 100) + "%");
    }

    @Override
    public long getHeaderId(int position) {
        if (position < Constants.SUPER_REPRESENTATIVE_COUNT) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RepresentativeListAdapter.RepresentativeHeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_representative_header, parent, false);
        return new RepresentativeHeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RepresentativeListAdapter.RepresentativeHeaderHolder holder, int position) {
        if (position < Constants.SUPER_REPRESENTATIVE_COUNT) {
            holder.headerText.setText(R.string.super_representatives_text);
            holder.headerText.setBackgroundResource(R.color.colorPrimaryDark);
        } else {
            holder.headerText.setText(R.string.super_representative_candidates_text);
            holder.headerText.setBackgroundResource(R.color.super_representative_background_color);
        }
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

    public class RepresentativeHeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.header_text)
        TextView headerText;

        public RepresentativeHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
