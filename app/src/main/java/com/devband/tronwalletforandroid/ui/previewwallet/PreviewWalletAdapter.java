package com.devband.tronwalletforandroid.ui.previewwallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewWalletAdapter extends RecyclerView.Adapter<PreviewWalletAdapter.AccountViewHolder> implements AdapterDataModel<TronWallet>, AdapterView {

    private List<TronWallet> mList = new ArrayList<>();

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public PreviewWalletAdapter(Context context, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mOnItemClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_transfer, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new PreviewWalletAdapter.AccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        TronWallet wallet = mList.get(position);

        holder.walletNameText.setText(wallet.getAccountName());

        // todo - get balance
        holder.balanceText.setText("TRX");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(TronWallet model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<TronWallet> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public TronWallet getModel(int position) {
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

    public TronWallet getItem(int pos) {
        return mList.get(pos);
    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.wallet_name_text)
        public TextView walletNameText;

        @BindView(R.id.balance_text)
        public TextView balanceText;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
