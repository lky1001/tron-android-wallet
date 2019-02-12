package com.devband.tronwalletforandroid.ui.mytransfer.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.Utils;
import com.devband.tronwalletforandroid.ui.mytransfer.dto.TransferInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2018. 5. 17..
 */

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.TransactionViewHolder> implements AdapterDataModel<TransferInfo>, AdapterView {

    private List<TransferInfo> mList = new ArrayList<>();

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public TransferAdapter(Context context, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mOnItemClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_transfer, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransferInfo info = mList.get(position);

        Date date = new Date(info.getTimestamp());

        double amount = (double) info.getAmount();

        if (info.getPrecision() > 0) {
            amount = (amount / Math.pow(10, info.getPrecision()));
        }

        if (info.isSend()) {
            Utils.setAccountDetailAction(mContext, holder.sendAddressText, info.getTransferToAddress());
            holder.sendAmountText.setText(Constants.tokenBalanceFormat.format(amount) + " " + info.getTokenName());
            holder.sendDateText.setText(Constants.sdf.format(date));

            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.receiveLayout.setVisibility(View.GONE);

            holder.copyToAddressView.setOnClickListener(v -> {
                    copyToClipboard(info.getTransferToAddress());
                    Toast.makeText(mContext, mContext.getString(R.string.copy_to_address_msg),
                            Toast.LENGTH_SHORT)
                            .show();
                });
        } else {
            Utils.setAccountDetailAction(mContext, holder.receiveAddressText, info.getTransferFromAddress());
            holder.receiveAmountText.setText(Constants.tokenBalanceFormat.format(amount) + " " + info.getTokenName());
            holder.receiveDateText.setText(Constants.sdf.format(date));

            holder.sendLayout.setVisibility(View.GONE);
            holder.receiveLayout.setVisibility(View.VISIBLE);

            holder.copyFromAddressView.setOnClickListener(v -> {
                    copyToClipboard(info.getTransferFromAddress());
                    Toast.makeText(mContext, mContext.getString(R.string.copy_from_address_msg),
                            Toast.LENGTH_SHORT)
                            .show();
                });
        }
    }

    private void copyToClipboard(String content) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", content);
        clipboard.setPrimaryClip(clip);
    }

    public TransferInfo getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refresh(List<TransferInfo> datas) {
        mList.clear();
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void add(TransferInfo model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<TransferInfo> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public TransferInfo getModel(int position) {
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

        @BindView(R.id.send_layout)
        LinearLayout sendLayout;

        @BindView(R.id.receive_layout)
        LinearLayout receiveLayout;

        @BindView(R.id.send_address_text)
        TextView sendAddressText;

        @BindView(R.id.receive_address_text)
        TextView receiveAddressText;

        @BindView(R.id.send_date_text)
        TextView sendDateText;

        @BindView(R.id.receive_date_text)
        TextView receiveDateText;

        @BindView(R.id.send_amount_text)
        TextView sendAmountText;

        @BindView(R.id.receive_amount_text)
        TextView receiveAmountText;

        @BindView(R.id.copy_from_address)
        ImageView copyFromAddressView;

        @BindView(R.id.copy_to_address)
        ImageView copyToAddressView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
