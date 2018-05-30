package com.devband.tronwalletforandroid.ui.accountdetail.representative;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.model.BaseModel;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.model.ViewType;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.viewholder.BaseViewHolder;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.viewholder.BlockStatViewHolder;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.viewholder.HeaderViewHolder;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.viewholder.TransferHistoryViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018. 5. 29..
 */

public class RepresentativeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<BaseModel> mList = new ArrayList<>();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewType type = ViewType.valueOf(viewType);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (type) {
            case BLOCK_STAT:
                return new BlockStatViewHolder(inflater.inflate(R.layout.list_item_block_stat, parent, false));
            case TRANSFER_HISTORY_ITEM:
                return new TransferHistoryViewHolder(inflater.inflate(R.layout.list_item_block_detail_transfer_history, parent, false));
            case HEADER:
            default:
                return new HeaderViewHolder(inflater.inflate(R.layout.list_item_header, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType().ordinal();
    }

    public void refresh(List<BaseModel> viewModels) {
        mList.clear();
        mList.addAll(viewModels);
        notifyDataSetChanged();
    }
}
