package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 30..
 */

public class RichListAdapter extends RecyclerView.Adapter<RichListAdapter.RichViewHolder> {

    private List<RichItemViewModel> mList = new ArrayList<>();

    @NonNull
    @Override
    public RichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rich, parent, false);
        return new RichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RichViewHolder holder, int position) {
        RichItemViewModel viewModel = mList.get(position);
        holder.txtBalance.setText(viewModel.getBalanceRange());
        holder.txtAddressPercentage.setText(viewModel.getAddressPercentage() + "%");
        holder.txtTrx.setText(viewModel.getCoins() + " TRX");
        holder.txtUsd.setText("---");
        holder.txtCoinPercentage.setText(viewModel.getCoinPercentage() + "%");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refresh(List<RichItemViewModel> viewModels) {
        mList.clear();
        mList.addAll(viewModels);
        notifyDataSetChanged();
    }

    public class RichViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.value_balnce)
        TextView txtBalance;


        @BindView(R.id.value_rich_address)
        TextView txtAddressPercentage;
        @BindView(R.id.contract_type_text)
        TextView txtTrx;
        @BindView(R.id.value_rich_usd)
        TextView txtUsd;
        @BindView(R.id.value_rich_coin)
        TextView txtCoinPercentage;


        public RichViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
