package com.devband.tronwalletforandroid.ui.token.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.devband.tronlib.dto.TokenHolder;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HolderAdapter extends RecyclerView.Adapter<HolderAdapter.TokenHolderViewHolder> implements AdapterDataModel<TokenHolder>, AdapterView {

    private List<TokenHolder> mList;

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public HolderAdapter(Context context, View.OnClickListener onItemClickListener) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TokenHolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_holder, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new TokenHolderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenHolderViewHolder holder, int position) {
        TokenHolder item = mList.get(position);

        holder.holderIdText.setText((position + 1) + ". ");
        Utils.setAccountDetailAction(mContext, holder.holderAddressText, item.getAddress());
        holder.holderBalanceText.setText(Constants.numberFormat.format(item.getBalance()));
        holder.holderBalancePercentText.setText(Constants.percentFormat.format(item.getBalancePercent()) + "%");
        holder.holderBalanceProgress.setMax((float) item.getTotalSupply());
        holder.holderBalanceProgress.setProgress((float) item.getBalance());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(TokenHolder model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<TokenHolder> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public TokenHolder getModel(int position) {
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

    class TokenHolderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.holder_address_id_text)
        public TextView holderIdText;

        @BindView(R.id.holder_address_text)
        public TextView holderAddressText;

        @BindView(R.id.holder_balance_text)
        public TextView holderBalanceText;

        @BindView(R.id.holder_balance_percent_text)
        public TextView holderBalancePercentText;

        @BindView(R.id.progress_balance)
        public RoundCornerProgressBar holderBalanceProgress;

        public TokenHolderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
