package com.devband.tronwalletforandroid.ui.blockexplorer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronlib.dto.Block;
import com.devband.tronlib.dto.Blocks;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 25..
 */

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.BlockViewHolder> {

    private List<Block> mList = new ArrayList<>();
    private View.OnClickListener mItemViewClickListener;

    public BlockAdapter(View.OnClickListener itemViewClickListener) {
        this.mItemViewClickListener = itemViewClickListener;
    }

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_block, parent, false);
        view.setOnClickListener(mItemViewClickListener);
        return new BlockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {
        Block block = mList.get(position);

        holder.txtNumber.setText("#" + Constants.numberFormat.format(block.getNumber()));
        holder.txtTransaction.setText(String.valueOf(block.getNrOfTrx()));

        Utils.setAccountDetailAction(holder.txtProducedBy.getContext(), holder.txtProducedBy, block.getWitnessAddress());

        Date date = new Date(block.getTimestamp());
        holder.txtTimestamp.setText(Constants.sdf.format(date));
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
        int beforeIndex = getItemCount();

        for (Block block : blocks.getData()) {
            if (!isContain(block)) {
                mList.add(block);
            }
        }
        notifyItemInserted(beforeIndex);
    }

    public Block getItem(int position) {
        return mList.get(position);
    }

    private boolean isContain(Block block) {
        for (Block b: mList) {
            if (b.getNumber() == block.getNumber()) {
                return true;
            }
        }

        return false;
    }

    public class BlockViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_number)
        TextView txtNumber;

        @BindView(R.id.txt_transaction)
        TextView txtTransaction;

        @BindView(R.id.txt_timestamp)
        TextView txtTimestamp;

        @BindView(R.id.txt_produced_by)
        TextView txtProducedBy;

        public BlockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
