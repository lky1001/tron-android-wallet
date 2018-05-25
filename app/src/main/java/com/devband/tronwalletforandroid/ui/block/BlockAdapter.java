package com.devband.tronwalletforandroid.ui.block;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronlib.dto.Block;
import com.devband.tronlib.dto.Blocks;
import com.devband.tronwalletforandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 25..
 */

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockViewHolder> {

    private List<Block> mList = new ArrayList<>();

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_market, parent, false);
        return new BlockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refresh(Blocks blocks) {
        mList.clear();
        mList.addAll(blocks.getData());
        notifyDataSetChanged();
    }

    public void addData(Blocks blocks) {
        int beforeIndex = getItemCount() - 1;
        mList.addAll(blocks.getData());
        notifyItemInserted(beforeIndex);
    }

    public class BlockViewHolder extends RecyclerView.ViewHolder {
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

        public BlockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
