package com.devband.tronwalletforandroid.ui.previewwallet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.database.model.AccountModel;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> implements AdapterDataModel<AccountModel>, AdapterView {

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder accountViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void add(AccountModel model) {

    }

    @Override
    public void addAll(List<AccountModel> list) {

    }

    @Override
    public void remove(int position) {

    }

    @Override
    public AccountModel getModel(int position) {
        return null;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public void refresh() {

    }

    class AccountViewHolder extends RecyclerView.ViewHolder {

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
