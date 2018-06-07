package com.devband.tronwalletforandroid.ui.accountdetail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronlib.dto.Transaction;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountTransactionAdapter extends RecyclerView.Adapter<AccountTransactionAdapter.TransactionViewHolder>  implements AdapterDataModel<Transaction>, AdapterView {

    private List<Transaction> mList = new ArrayList<>();

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    private boolean mViewAddressDetail;

    public AccountTransactionAdapter(Context context, View.OnClickListener onClickListener, boolean viewAddressDetail) {
        this.mContext = context;
        this.mOnItemClickListener = onClickListener;
        this.mViewAddressDetail = viewAddressDetail;
    }

    @NonNull
    @Override
    public AccountTransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transaction, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new AccountTransactionAdapter.TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountTransactionAdapter.TransactionViewHolder holder, int position) {
        Transaction item = mList.get(position);

        Date date = new Date(item.getTimestamp());

        holder.hashText.setText(item.getHash());
        Utils.setBlockDetailAction(mContext, holder.blockNumberText, item.getBlock());

        if (mViewAddressDetail) {
            Utils.setAccountDetailAction(mContext, holder.addressText, item.getOwnerAddress());
        } else {
            holder.addressText.setText(item.getOwnerAddress());
        }

        holder.createdText.setText(Constants.sdf.format(date));
        holder.contractTypeText.setText(Utils.getContractTypeString(mContext, item.getContractType()));
    }

    public Transaction getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refresh(List<Transaction> datas) {
        mList.clear();
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void add(Transaction model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<Transaction> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public Transaction getModel(int position) {
        return mList.get(position);
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hash_text)
        TextView hashText;

        @BindView(R.id.block_number_text)
        TextView blockNumberText;

        @BindView(R.id.address_text)
        TextView addressText;

        @BindView(R.id.contract_type_text)
        TextView contractTypeText;

        @BindView(R.id.created_text)
        TextView createdText;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
