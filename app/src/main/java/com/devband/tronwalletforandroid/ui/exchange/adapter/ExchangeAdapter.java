package com.devband.tronwalletforandroid.ui.exchange.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.tron.dto.ExchangeDto;

import java.util.List;

import butterknife.ButterKnife;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder> implements AdapterDataModel<ExchangeDto>, AdapterView {

    private List<ExchangeDto> mList;

    public ExchangeAdapter(List<ExchangeDto> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeViewHolder exchangeViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void add(ExchangeDto model) {
        mList.add(model);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<ExchangeDto> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ExchangeDto getModel(int position) {
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

    class ExchangeViewHolder extends RecyclerView.ViewHolder {

        public ExchangeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
