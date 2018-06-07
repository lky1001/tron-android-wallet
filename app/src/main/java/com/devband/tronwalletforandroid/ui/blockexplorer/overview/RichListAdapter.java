package com.devband.tronwalletforandroid.ui.blockexplorer.overview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.Utils;

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
        holder.txtBalanceRange.setText(viewModel.getBalanceRange());
        holder.txtAddressPercentage.setText(Utils.getCommaNumber(viewModel.getAddressCount())
                + "(" + Utils.getPercentFormat(viewModel.getAddressPercentage()) + "%)");
        holder.txtTrx.setText(Utils.getTrxFormat(viewModel.getCoins()) + " TRX ("
                + Utils.getPercentFormat(viewModel.getCoinPercentage()) + "%)");
        holder.txtUsd.setText(Utils.getUsdFormat(viewModel.getUsd()));
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
        TextView txtBalanceRange;

        @BindView(R.id.value_rich_address)
        TextView txtAddressPercentage;

        @BindView(R.id.contract_type_text)
        TextView txtTrx;

        @BindView(R.id.value_rich_usd)
        TextView txtUsd;

        public RichViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
