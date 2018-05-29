package com.devband.tronwalletforandroid.ui.accountdetail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devband.tronlib.dto.AccountVote;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.VoteViewHolder>  implements AdapterDataModel<AccountVote>, AdapterView {

    private List<AccountVote> mList = new ArrayList<>();

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public VoteAdapter(Context context, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mOnItemClickListener = onClickListener;
    }

    @NonNull
    @Override
    public VoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_account_vote, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new VoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteViewHolder holder, int position) {
        AccountVote item = mList.get(position);
    }

    public AccountVote getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refresh(List<AccountVote> datas) {
        mList.clear();
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void add(AccountVote model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<AccountVote> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public AccountVote getModel(int position) {
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

    public class VoteViewHolder extends RecyclerView.ViewHolder {

        public VoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
