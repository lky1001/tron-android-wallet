package com.devband.tronwalletforandroid.ui.main.adapter;

import java.util.List;

public interface AdapterDataModel<T> {

    void add(T model);
    void addAll(List<T> list);
    void remove(int position);
    T getModel(int position);
    int getSize();
    void clear();
}
