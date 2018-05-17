package com.devband.tronwalletforandroid.ui.tronaccount.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.ui.tronaccount.dto.TronAccount;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TronAccountListAdapter extends RecyclerView.Adapter<TronAccountListAdapter.TronAccountViewHolder> implements AdapterDataModel<TronAccount>, AdapterView {

    private List<TronAccount> mList;

    private Context mContext;

    private DecimalFormat df = new DecimalFormat("#,##0");

    public TronAccountListAdapter(Context mContext) {
        this.mList = new ArrayList<>();
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TronAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tron_account, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        return new TronAccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TronAccountViewHolder holder, int position) {
        TronAccount item = mList.get(position);

        holder.addressText.setText(item.getAddress());
        holder.trxText.setText(df.format(item.getBalance() / Constants.REAL_TRX_AMOUNT));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(TronAccount model) {
        mList.add(model);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void addAll(List<TronAccount> list) {
        mList.addAll(list);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public TronAccount getModel(int position) {
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

    public class TronAccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.address_text)
        TextView addressText;

        @BindView(R.id.trx_text)
        TextView trxText;

        public TronAccountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
