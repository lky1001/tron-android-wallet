package com.devband.tronwalletforandroid.ui.accountdetail.representative;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.CommonFragment;
import com.devband.tronwalletforandroid.ui.accountdetail.AccountDetailActivity;
import com.devband.tronwalletforandroid.ui.accountdetail.representative.model.BaseModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepresentativeFragment extends CommonFragment implements RepresentativeView {

    @Inject
    RepresentativePresenter mRepresentativePresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private RepresentativeAdapter mAdapter;

    private String mAddress;

    public static Fragment newInstance(@NonNull String address) {
        RepresentativeFragment fragment = new RepresentativeFragment();
        Bundle args = new Bundle(1);
        args.putString(AccountDetailActivity.EXTRA_ADDRESS, address);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_transaction, container, false);
        ButterKnife.bind(this, view);
        initUi();

        mAddress = getArguments().getString(AccountDetailActivity.EXTRA_ADDRESS);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUi() {
        mAdapter = new RepresentativeAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void refresh() {
        if (isAdded()) {
            mRepresentativePresenter.loadData(mAddress);
        }
    }

    @Override
    public void dataLoadSuccess(List<BaseModel> viewModels) {
        hideDialog();
        if (mAdapter != null && isAdded()) {
            mAdapter.refresh(viewModels);
        }
    }

    @Override
    public void showLoadingDialog() {
        showProgressDialog(null, getString(R.string.loading_msg));
    }

    @Override
    public void showServerError() {
        if (isAdded()) {
            hideDialog();
            Toast.makeText(getActivity(), getString(R.string.connection_error_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
