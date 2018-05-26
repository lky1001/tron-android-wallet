package com.devband.tronwalletforandroid.ui.market;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronlib.dto.Market;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 24..
 */

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> {

    private List<Market> mList = new ArrayList<>();

    @NonNull
    @Override
    public MarketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_market, parent, false);
        return new MarketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketViewHolder holder, int position) {
        Market market = mList.get(position);
        holder.txtMarket.setText(market.getName());
        holder.txtPair.setText(market.getPair());
        holder.txtVolume.setText(Constants.numberFormat.format(market.getVolume()) + " TRX");
        holder.txtPercentage.setText("(" + Constants.percentFormat.format(market.getVolumePercentage()) + "%)");
        holder.txtPrice.setText("$" + Constants.usdFormat.format(market.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refresh(List<Market> items) {
        mList.clear();
        mList.addAll(items);
        notifyDataSetChanged();
    }

    public class MarketViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_market)
        TextView txtMarket;

        @BindView(R.id.txt_pair)
        TextView txtPair;

        @BindView(R.id.txt_volume)
        TextView txtVolume;

        @BindView(R.id.txt_percentage)
        TextView txtPercentage;

        @BindView(R.id.txt_price)
        TextView txtPrice;

        public MarketViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
