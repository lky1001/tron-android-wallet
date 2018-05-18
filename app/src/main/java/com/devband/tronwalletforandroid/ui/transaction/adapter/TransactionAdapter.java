package com.devband.tronwalletforandroid.ui.transaction.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.ui.transaction.dto.TransactionInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 17..
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<TransactionInfo> mList = new ArrayList<>();

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        //TODO bindViewHolder
        TransactionInfo info = mList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
        Date date = new Date(info.getTimestamp());
        holder.time.setText(sdf.format(date));

        holder.hash.setText(info.getHash());
        holder.block.setText(info.getBlock() + "");

        holder.token.setText(info.getTokenName());
        holder.amount.setText(info.getAmount() + "");

        holder.fromAddress.setText(info.getTransferFromAddress());
        holder.toAddress.setText(info.getTransferToAddress());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refresh(List<TransactionInfo> datas) {
        mList.clear();
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_time)
        TextView time;
        @BindView(R.id.txt_hash)
        TextView hash;
        @BindView(R.id.txt_block)
        TextView block;
        @BindView(R.id.txt_amount)
        TextView amount;
        @BindView(R.id.txt_token)
        TextView token;
        @BindView(R.id.txt_from_address)
        TextView fromAddress;
        @BindView(R.id.txt_to_address)
        TextView toAddress;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
