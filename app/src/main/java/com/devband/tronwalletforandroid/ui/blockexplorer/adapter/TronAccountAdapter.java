package com.devband.tronwalletforandroid.ui.blockexplorer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.devband.tronlib.dto.TronAccount;
import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TronAccountAdapter extends RecyclerView.Adapter<TronAccountAdapter.TokenHolderViewHolder> implements AdapterDataModel<TronAccount>, AdapterView {

    private List<TronAccount> mList;

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public TronAccountAdapter(Context context, View.OnClickListener onItemClickListener) {
        this.mList = new ArrayList<>();
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public TokenHolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tron_account, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new TokenHolderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TokenHolderViewHolder holder, int position) {
        TronAccount item = mList.get(position);

        holder.tronAddressNoText.setText((position + 1) + ".");
        Utils.setAccountDetailAction(mContext, holder.tronAddressText, item.getAddress());
        holder.tronBalanceText.setText(Constants.tokenBalanceFormat.format(item.getBalance() / Constants.ONE_TRX) + " " + Constants.TRON_SYMBOL);
        holder.tronBalancePercentText.setText(Constants.percentFormat.format(item.getBalancePercent()) + "%");
        holder.tronBalanceProgress.setMax((float) item.getAvailableSypply());
        holder.tronBalanceProgress.setProgress((float) ((double) item.getBalance() / Constants.ONE_TRX));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(TronAccount model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<TronAccount> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
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
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    class TokenHolderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tron_address_no_text)
        public TextView tronAddressNoText;

        @BindView(R.id.tron_address_text)
        public TextView tronAddressText;

        @BindView(R.id.tron_balance_text)
        public TextView tronBalanceText;

        @BindView(R.id.tron_balance_percent_text)
        public TextView tronBalancePercentText;

        @BindView(R.id.progress_balance)
        public RoundCornerProgressBar tronBalanceProgress;

        public TokenHolderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
